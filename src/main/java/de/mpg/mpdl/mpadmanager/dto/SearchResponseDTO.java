package de.mpg.mpdl.mpadmanager.dto;

import java.util.List;

public class SearchResponseDTO {
  int resultCount;
  List<SearchItem> records;

  public int getResultCount() {
    return this.resultCount;
  }

  public void setResultCount(int resultCount) {
    this.resultCount = resultCount;
  }

  public List<SearchItem> getRecords() {
    return this.records;
  }

  public void setRecords(List<SearchItem> records) {
    this.records = records;
  }
}