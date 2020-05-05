package de.mpg.mpdl.mpadmanager.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordDTO {

  @SerializedName("abstract")
  @Expose
  private String abs;
  
  private List<String> authorsPrimary;
  private String id;
  private List<String> isbns;
  private String title;
  @JsonProperty("downloadUrl")
  private String urlPdf_str;
  private List<String> publicationDates;
  private List<String> publishers;
  private boolean isPdf;
  private String thumbnail;

  public String getAbs() {
    return this.abs;
  }

  public void setAbs(String abs) {
    this.abs = abs;
  }

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

  public String getDownloadUrl() {
    return this.urlPdf_str;
  }

  public void setDownloadUrl(String urlPdf_str) {
    this.urlPdf_str = urlPdf_str;
  }

  public List<String> getPublicationDates() {
    return this.publicationDates;
  }

  public void setPublicationDates(List<String> publicationDates) {
    this.publicationDates = publicationDates;
  }

  public List<String> getPublishers() {
    return this.publishers;
  }

  public void setPublishers(List<String> publishers) {
    this.publishers = publishers;
  }

  public boolean getIsPdf() {
    return this.isPdf;
  }

  public void setIsPdf(boolean isPdf) {
    this.isPdf = isPdf;
  }


  public String getThumbnail() {
    return this.thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }


}