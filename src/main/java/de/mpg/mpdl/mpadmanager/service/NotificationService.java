package de.mpg.mpdl.mpadmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService{
  
  @Autowired
  private JavaMailSender javaMailSender;

  @Async
  public void sendNotification(SimpleMailMessage message) throws MailException, InterruptedException {
    javaMailSender.send(message);
  }
}