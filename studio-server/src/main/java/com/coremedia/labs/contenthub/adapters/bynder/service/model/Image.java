package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image extends Entity {

  public static final String TYPE = "image";

  @JsonProperty("transformBaseUrl")
  private String transformBaseUrl;

  public String getTransformBaseUrl() {
    return transformBaseUrl;
  }

  public void setTransformBaseUrl(String transformBaseUrl) {
    this.transformBaseUrl = transformBaseUrl;
  }

  public enum Orientation {
    landscape, portrait, square
  }
}
