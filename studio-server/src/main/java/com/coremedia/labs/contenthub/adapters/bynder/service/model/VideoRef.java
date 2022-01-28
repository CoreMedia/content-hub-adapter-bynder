package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoRef {

  @JsonProperty("url")
  private String url;

  @JsonProperty("width")
  private int width;

  @JsonProperty("height")
  private int height;

  @JsonProperty("size")
  private int size;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }
}
