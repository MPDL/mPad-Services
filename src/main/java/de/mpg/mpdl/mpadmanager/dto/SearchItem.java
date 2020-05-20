package de.mpg.mpdl.mpadmanager.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchItem {
  private List<String> authorsPrimary;
  
  @JsonIgnore
  private List<String> authorsSecondary;
  private String id;
  private List<String> isbns;
  private String title;
  private List<String> publicationDates;
  private List<String> publishers;
  private String thumbnail;

  public List<String> getAuthorsPrimary() {
    return this.authorsPrimary;
  }

  public void setAuthorsPrimary(List<String> authorsPrimary) {
    this.authorsPrimary = authorsPrimary;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getIsbns() {
    return this.isbns;
  }

  public void setIsbns(List<String> isbns) {
    this.isbns = isbns;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getPublicationDates() {
    return this.publicationDates;
  }

  public void setPublicationDates(List<String> publicationDates) {
    this.publicationDates = publicationDates;
  }

  public List<String> getAuthorsSecondary() {
    return this.authorsSecondary;
  }

  public void setAuthorsSecondary(List<String> authorsSecondary) {
    this.authorsSecondary = authorsSecondary;
  }

  public List<String> getPublishers() {
    return this.publishers;
  }

  public void setPublishers(List<String> publishers) {
    this.publishers = publishers;
  }

  public String getThumbnail() {
    return this.thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }
}