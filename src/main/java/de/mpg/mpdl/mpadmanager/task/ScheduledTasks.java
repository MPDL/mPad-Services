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
	
	@Scheduled(fixedRate = 60000)
	public void reportCurrentTime() {
		Date now = new Date();
        List<VerificationToken> tokens = userService.findExpiredTokens(now);
        
        for(VerificationToken token: tokens) {
            String userEmail = token.getUserEmail();
            User user = userService. findUserByEmail(userEmail);
        	if (!user.isEnabled()) {
        		if (token.isExpiredOnce()) {
	                userService.deleteUser(user);
	                userService.deleteVerificationToken(token.getToken());
        		} else { // resent token
                    final VerificationToken newToken = userService.generateNewVerificationToken(token.getToken());
                    String host = messages.getMessage("message.appurl", null, new Locale("en"));
        	        mailSender.send(constructResendVerificationTokenEmail(host, newToken, user));
        		}
        	}
        }
	}
	
    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath,final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();

        final String subject = messages.getMessage("message.expired.mail.subject", null, new Locale("en"));
        final String recipientAddress = user.getEmail();
        final String message_1 = messages.getMessage("message.dear", null, new Locale("en")) + user.getFirstName() + ",\r\n\n" + messages.getMessage("message.expired.mail_1", null, new Locale("en"));
        final String message_2 = messages.getMessage("message.expired.mail_2", null, new Locale("en"));
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message_1+ " \r\n" + confirmationUrl + " \r\n\n" + message_2);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
