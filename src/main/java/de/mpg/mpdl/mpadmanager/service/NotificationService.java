package de.mpg.mpdl.mpadmanager.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.mpg.mpdl.mpadmanager.vo.MailVO;

@Service
public class NotificationService implements INotificationService{
  
  @Autowired
  private JavaMailSender javaMailSender;

  @Async
  public void sendNotification(SimpleMailMessage message) throws MailException, InterruptedException {
    javaMailSender.send(message);
  }

  @Async
  public void sendMimeNotification(MailVO mailVo) throws MailException, InterruptedException, MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
    messageHelper.setFrom(mailVo.getFrom());
    messageHelper.setTo(mailVo.getTo().split(","));
    messageHelper.setSubject(mailVo.getSubject());
    messageHelper.setText(mailVo.getText(), true);
    javaMailSender.send(mimeMessage);
  }
}