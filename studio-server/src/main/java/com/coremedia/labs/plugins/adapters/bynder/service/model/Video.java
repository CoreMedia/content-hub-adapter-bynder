package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Video extends Entity {

  public static final String TYPE = "video";

  @JsonProperty("videoPreviewURLs")
  private String[] urls;

  public String getVideoPreviewURL() {
    if (urls != null && urls.length > 0) {
      return urls[0];
    } else {
      return "";
    }
  }

  public void setVideoPreviewUrls(String[] urls) {
    this.urls = urls;
  }

}
