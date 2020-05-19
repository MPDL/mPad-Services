package de.mpg.mpdl.mpadmanager.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.model.VerificationToken;
import de.mpg.mpdl.mpadmanager.service.INotificationService;
import de.mpg.mpdl.mpadmanager.service.IUserService;
import de.mpg.mpdl.mpadmanager.vo.MailVO;


@Component
public class ScheduledTasks { 
    
    @Autowired
    private IUserService userService;

    @Autowired
	private INotificationService notificationService;
    
    @Autowired
    private MessageSource messages;
    
    @Autowired
    private Environment env;

    @Autowired
	private TemplateEngine templateEngine;
	
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
                    final MailVO email = constructResendVerificationTokenEmail(host, newToken, user);
                    try {
                        notificationService.sendMimeNotification(email);
                    } catch (MailException | InterruptedException | MessagingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        		}
        	}
        }
	}

    private final MailVO constructResendVerificationTokenEmail(final String contextPath,final VerificationToken newToken, final User user) {
        final String subject = messages.getMessage("message.expired.mail.subject", null, new Locale("en"));
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();

        final String firstName = user.getFirstName();
        final Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("confirmationUrl", confirmationUrl);
        final String body = templateEngine.process("mail/resendVerificationTokenEmail", context);

        final MailVO email = new MailVO();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
    }
}
