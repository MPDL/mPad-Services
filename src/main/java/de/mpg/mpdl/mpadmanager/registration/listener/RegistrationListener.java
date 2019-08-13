package de.mpg.mpdl.mpadmanager.registration.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import de.mpg.mpdl.mpadmanager.model.User;
import de.mpg.mpdl.mpadmanager.registration.OnRegistrationCompleteEvent;
import de.mpg.mpdl.mpadmanager.service.IUserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	
	@Autowired
	private IUserService service;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private Environment env;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
	}
	
	private void confirmRegistration(final OnRegistrationCompleteEvent event) {
		final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user.getEmail(), token);
        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
	}
	
    private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
        final String message_1 = messages.getMessage("message.dear", null, event.getLocale()) + user.getFirstName() + ",\r\n\n" + messages.getMessage("message.regSucc_1", null, event.getLocale());
        final String message_2 = messages.getMessage("message.regSucc_2", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message_1+ " \r\n" + confirmationUrl + " \r\n\n" + message_2);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
	
}
