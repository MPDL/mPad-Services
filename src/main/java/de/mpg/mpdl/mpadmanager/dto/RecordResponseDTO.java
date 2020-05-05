package de.mpg.mpdl.mpadmanager.dto;

import java.util.List;

public class RecordResponseDTO {
  int resultCount;
  List<RecordDTO> records;


  public int getResultCount() {
    return this.resultCount;
  }

  public void setResultCount(int resultCount) {
    this.resultCount = resultCount;
  }

  public List<RecordDTO> getRecords() {
    return this.records;
  }

  public void setRecords(List<RecordDTO> records) {
    this.records = records;
  }


}