package de.mpg.mpdl.mpadmanager.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.web.multipart.MultipartFile;

public class MailVO {
  private String id;
  private String from;
  private String to;
  private String subject;
  private String text;
  private Date sentDate;
  private String status;
  private String error;
  @JsonIgnore
  private MultipartFile[] multipartFiles;


  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return this.to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getSubject() {
    return this.subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getSentDate() {
    return this.sentDate;
  }

  public void setSentDate(Date sentDate) {
    this.sentDate = sentDate;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getError() {
    return this.error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public MultipartFile[] getMultipartFiles() {
    return this.multipartFiles;
  }

  public void setMultipartFiles(MultipartFile[] multipartFiles) {
    this.multipartFiles = multipartFiles;
  }

}