package de.mpg.mpdl.mpadmanager.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public interface INotificationService {
  public void sendNotification(SimpleMailMessage message) throws MailException, InterruptedException;
}