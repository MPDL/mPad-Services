package de.mpg.mpdl.mpadmanager.task;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.model.VerificationToken;
import de.mpg.mpdl.mpadmanager.service.IUserService;
import de.mpg.mpdl.mpadmanager.web.util.GenericResponse;


@Component
public class ScheduledTasks {
	
    @Autowired
    private JavaMailSender mailSender;   
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private MessageSource messages;
    
    @Autowired
    private Environment env;
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	
	@Scheduled(fixedRate = 100000)
	public void reportCurrentTime() {
		Date now = new Date();
        log.info("The time is now {}", dateFormat.format(now));

        List<VerificationToken> tokens = userService.findExpiredTokens(now);
        
        log.info("token count: " + tokens.size());

        for(VerificationToken token: tokens) {
            String userEmail = token.getUserEmail();
            User user = userService. findUserByEmail(userEmail);
        	if (!user.isEnabled()) {
        		if (token.isExpiredOnce()) {
	        		mailSender.send(constructVerificationTokenExpiredEmail(user));
	                userService.deleteUser(user); // delete user in LDAP server
	                userService.deleteVerificationToken(token.getToken());
        		} else { // resent token
        	        final VerificationToken newToken = userService.generateNewVerificationToken(token.getToken());
        	        mailSender.send(constructResendVerificationTokenEmail("http://vm116.mpdl.mpg.de", newToken, user));
                    //mailSender.send(constructResendVerificationTokenEmail("http://localhost:8080", newToken, user));
        		}
        	}
        }
	}
	
    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath,final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, new Locale("en"));
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }
	
    private SimpleMailMessage constructVerificationTokenExpiredEmail(final User user) {
        return constructEmail("Registration Token expired", "Your verification token is expired, please try to register again", user);
    }
	
    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
