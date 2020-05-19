package de.mpg.mpdl.mpadmanager.registration.listener;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.registration.OnRegistrationCompleteEvent;
import de.mpg.mpdl.mpadmanager.service.INotificationService;
import de.mpg.mpdl.mpadmanager.service.IUserService;
import de.mpg.mpdl.mpadmanager.vo.MailVO;

import javax.mail.MessagingException;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private IUserService service;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;

    @Autowired
	private TemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user.getEmail(), token);
        final MailVO email = constructRegistrationEmail(event, user, token);

        try {
            notificationService.sendMimeNotification(email);
        } catch (MailException | InterruptedException | MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

    private final MailVO constructRegistrationEmail(final OnRegistrationCompleteEvent event, final User user,  final String token) {
        final String subject = messages.getMessage("message.registration.mail.subject", null, event.getLocale());
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;

        final String firstName = user.getFirstName();
        final Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("confirmationUrl", confirmationUrl);
        final String body = templateEngine.process("mail/registrationLinkEmail", context);

        final MailVO email = new MailVO();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
    }
	
}
