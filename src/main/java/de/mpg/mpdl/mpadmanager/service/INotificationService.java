package de.mpg.mpdl.mpadmanager.service;

import javax.mail.MessagingException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

import de.mpg.mpdl.mpadmanager.vo.MailVO;

public interface INotificationService {
  public void sendNotification(SimpleMailMessage message) throws MailException, InterruptedException;
  public void sendMimeNotification(MailVO mailVo) throws MailException, InterruptedException, MessagingException;
}