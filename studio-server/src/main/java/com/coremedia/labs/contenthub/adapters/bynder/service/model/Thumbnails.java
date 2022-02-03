package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Thumbnails {

  @JsonProperty("mini")
  private String miniUrl;

  @JsonProperty("webimage")
  private String webImageUrl;

  public String getMiniUrl() {
    return miniUrl;
  }

  public void setMiniUrl(String miniUrl) {
    this.miniUrl = miniUrl;
  }

  public String getWebImageUrl() {
    return webImageUrl;
  }

  public void setWebImageUrl(String webImageUrl) {
    this.webImageUrl = webImageUrl;
  }
}
