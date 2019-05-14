package de.mpg.mpdl.mpadmanager.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.mpg.mpdl.mpadmanager.dto.UserDTO;
import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.model.VerificationToken;
import de.mpg.mpdl.mpadmanager.registration.OnRegistrationCompleteEvent;
import de.mpg.mpdl.mpadmanager.service.IUserService;
import de.mpg.mpdl.mpadmanager.web.util.GenericResponse;

@Controller
public class RegistrationController {
	 private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	    @Autowired
	    private IUserService userService;

	    @Autowired
	    private MessageSource messages;

	    @Autowired
	    private JavaMailSender mailSender;

	    @Autowired
	    private ApplicationEventPublisher eventPublisher;

	    @Autowired
	    private Environment env;

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    public RegistrationController() {
	        super();
	    }

	    @GetMapping("/")
	    public String indexPage() {
	        return "redirect:/registration.html";
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
	    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
	        Locale locale = request.getLocale();
	        final String result = userService.validateVerificationToken(token);
	        if (result.equals("valid")) {
	            final User user = userService.getUser(token);
	            authWithoutPassword(user);
	            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
	            userService.deleteVerificationToken(token);
	            return "redirect:/successActivate.html";
	        }

	        model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
	        model.addAttribute("expired", "expired".equals(result));
	        model.addAttribute("token", token);
	        return "redirect:/badUser.html?lang=" + locale.getLanguage();
	    }

	    // user activation - verification

	    @RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
	    @ResponseBody
	    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
	        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
	        final User user = userService.getUser(newToken.getToken());
	        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
	        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
	    }

	    // ============== NON-API ============

	    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
	        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
	        final String message = messages.getMessage("message.resendToken", null, locale);
	        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
	    }

	    private SimpleMailMessage constructEmail(String subject, String body, User user) {
	        final SimpleMailMessage email = new SimpleMailMessage();
	        email.setSubject(subject);
	        email.setText(body);
	        email.setTo(user.getEmail());
	        email.setFrom(env.getProperty("support.email"));
	        return email;
	    }

	    private String getAppUrl(HttpServletRequest request) {
	        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	    }

	    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
	        try {
	            request.login(username, password);
	        } catch (ServletException e) {
	            LOGGER.error("Error while login ", e);
	        }
	    }

	    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
	        authToken.setDetails(new WebAuthenticationDetails(request));
	        Authentication authentication = authenticationManager.authenticate(authToken);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        // request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
	    }
	    
	    public void authWithoutPassword(User user) {
	        Stream<String> privileges = Stream.of("READ_PRIVILEGE", "CHANGE_PASSWORD_PRIVILEGE");
	        List<GrantedAuthority> authorities = privileges.map(p -> new SimpleGrantedAuthority(p)).collect(Collectors.toList());

	        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	    }
}
