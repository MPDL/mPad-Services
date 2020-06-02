package de.mpg.mpdl.mpadmanager.controller;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mpg.mpdl.mpadmanager.dto.PasswordDTO;
import de.mpg.mpdl.mpadmanager.dto.UserDTO;
import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.model.VerificationToken;
import de.mpg.mpdl.mpadmanager.registration.OnRegistrationCompleteEvent;
import de.mpg.mpdl.mpadmanager.service.INotificationService;
import de.mpg.mpdl.mpadmanager.service.ISecurityUserService;
import de.mpg.mpdl.mpadmanager.service.IUserService;
import de.mpg.mpdl.mpadmanager.vo.MailVO;
import de.mpg.mpdl.mpadmanager.web.util.GenericResponse;
import springfox.documentation.annotations.ApiIgnore;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.springframework.mail.MailException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
@ApiIgnore
@Controller
public class RegistrationController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserService userService;

	@Autowired
	private ISecurityUserService securityUserService;

	@Autowired
	private MessageSource messages;

	@Autowired
	private INotificationService notificationService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private Environment env;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TemplateEngine templateEngine;

	public RegistrationController() {
		super();
	}

	// Registration
	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse registerUserAccount(@Valid final UserDTO accountDto, final HttpServletRequest request) {
		LOGGER.debug("Registering user account with information: {}", accountDto);

		final User registered = userService.registerNewUserAccount(accountDto);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
		return new GenericResponse("success");
	}

	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(final HttpServletRequest request, final Model model,
			@RequestParam("token") final String token) throws UnsupportedEncodingException {
		final Locale locale = request.getLocale();
		final String result = userService.validateVerificationToken(token);
		LOGGER.info("token: " + result);
		if (result.equals("valid")) {
			final User user = userService.getUser(token);
			// authWithoutPassword(user);
			try {
				notificationService.sendMimeNotification(constructSucActivateEmail(getAppUrl(request), request.getLocale(), user));
				notificationService.sendNotification(constructShippingInfoEmail(getAppUrl(request), request.getLocale(), user));
			} catch (MailException | InterruptedException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
			userService.deleteVerificationToken(token);
			LOGGER.info("next: redirect:/successActivate.html");
			return "redirect:/successActivate.html";
		}

		model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
		model.addAttribute("expired", "expired".equals(result));
		model.addAttribute("token", token);
		LOGGER.info("next: redirect:/badUser.html");
		return "redirect:/badUser.html?lang=" + locale.getLanguage();
	}

	// Reset password
	@RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
			final User user = userService.findUserByEmail(userEmail);
			if (user != null) {
					if (!user.isEnabled()) return new GenericResponse(messages.getMessage("auth.message.disabled", null, request.getLocale()));	
					final String token = UUID.randomUUID().toString();
					userService.createPasswordResetTokenForUser(user, token);
					try {
						notificationService.sendMimeNotification(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
					} catch (MailException | InterruptedException | MessagingException e) {
						e.printStackTrace();
					}
					return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
			} else return new GenericResponse(messages.getMessage("auth.message.invalidUser", null, request.getLocale()));
	}

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
			final String result = securityUserService.validatePasswordResetToken(id, token);
			if (result != null) {
					model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
					model.addAttribute("title", "Registration");
					return "redirect:/login?lang=" + locale.getLanguage();
			}
			return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
	}

	@RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse savePassword(final Locale locale, final PasswordDTO passwordDto) {
			final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null) {
				userService.changeUserPassword(user, passwordDto.getNewPassword());
				return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
			}
			return new GenericResponse(messages.getMessage("message.error", null, locale));
	}

	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse deleteUser(final Locale locale, final String email) {
		final User user = userService.findUserByEmail(email);
		userService.deleteUser(user);
		return new GenericResponse("deleted");
	}

	// ============== NON-API ============

	private MailVO constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
			final String tokenUrl = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
			final String subject = messages.getMessage("message.resetYourPassword", null, locale);
			final String firstName = user.getFirstName();
			final Context context = new Context();
			context.setVariable("firstName", firstName);
			context.setVariable("tokenUrl", tokenUrl);
			String body = templateEngine.process("mail/resetPasswordEmail", context);
			return constructMimeEmailVO(subject, body, user);
	}

	private SimpleMailMessage constructShippingInfoEmail(final String contextPath, final Locale locale, final User user) {
			final String message = "Email: " + user.getEmail()
														+ "\r\nName: " + (user.getTitle()==null ? "": user.getTitle() + " ") + user.getFirstName() + " " + user.getLastName()
														+ "\r\nAddress: " + user.getAddress() + ", " +user.getZip()
														+ "\r\nTelephone: " + user.getTelephone();
			final String subject = messages.getMessage("message.ship.mail.subject", null, locale);
			return constructEmail(subject, message, user, "mpadadmin@mpdl.mpg.de");
	}

	public MailVO constructSucActivateEmail(final String contextPath, final Locale locale, final User user) {
			final String subject = messages.getMessage("message.padOnTheWay.mail.subject", null, locale);
			final String firstName = user.getFirstName();
			final Context context = new Context();
			context.setVariable("firstName", firstName);
			String body = templateEngine.process("mail/sucActivateEmail", context);
			return constructMimeEmailVO(subject, body, user);
	}

	private MailVO constructMimeEmailVO(final String subject, final String body, final User user) {
		final MailVO email = new MailVO();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	private SimpleMailMessage constructEmail(final String subject, final String body, final User user, final String receiverEmail) {
			final SimpleMailMessage email = new SimpleMailMessage();
			email.setSubject(subject);
			email.setText(body);
			email.setTo(receiverEmail);
			email.setFrom(env.getProperty("support.email"));
			return email;
}

	private String getAppUrl(final HttpServletRequest request) {
			if(request.getServerPort() != 80) {
				return  "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			}
			return  "https://" + request.getServerName() + request.getContextPath();
	}

	public void authWithHttpServletRequest(final HttpServletRequest request, final String username, final String password) {
			try {
					request.login(username, password);
			} catch (final ServletException e) {
					LOGGER.error("Error while login ", e);
			}
	}

	public void authWithAuthManager(final HttpServletRequest request, final String username, final String password) {
			final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			authToken.setDetails(new WebAuthenticationDetails(request));
			final Authentication authentication = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
	}

	public void authWithoutPassword(final User user) {
			final Stream<String> privileges = Stream.of("READ_PRIVILEGE", "CHANGE_PASSWORD_PRIVILEGE");
			final List<GrantedAuthority> authorities = privileges.map(p -> new SimpleGrantedAuthority(p)).collect(Collectors.toList());

			final Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}