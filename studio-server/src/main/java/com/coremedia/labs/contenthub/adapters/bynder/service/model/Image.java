package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image extends Entity {

  public static final String TYPE = "image";

  @JsonProperty("transformBaseUrl")
  private String transformBaseUrl;

  @JsonProperty("width")
  private int width;

  @JsonProperty("height")
  private int height;

  public String getTransformBaseUrl() {
    return transformBaseUrl;
  }

  public void setTransformBaseUrl(String transformBaseUrl) {
    this.transformBaseUrl = transformBaseUrl;
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

  public enum Orientation {
    landscape, portrait, square
  }
}
