package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Video extends Entity {

  public static final String TYPE = "video";

  @JsonProperty("videoPreviewURLs")
  private String[] urls;

  @JsonProperty("width")
  private int width;

  @JsonProperty("height")
  private int height;

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

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
