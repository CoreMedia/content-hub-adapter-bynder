package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Download {

  @JsonProperty("s3_file")
  private String s3File;

  public String getS3File() {
    return s3File;
  }

  public void setS3File(String s3File) {
    this.s3File = s3File;
  }
}
