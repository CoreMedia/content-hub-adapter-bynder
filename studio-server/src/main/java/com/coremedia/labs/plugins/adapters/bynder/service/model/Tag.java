package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag {

  @JsonProperty("id")
  private String id;

  @JsonProperty("tag")
  private String tag;

  @JsonProperty("mediaCount")
  private int mediaCount;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public int getMediaCount() {
    return mediaCount;
  }

  public void setMediaCount(int mediaCount) {
    this.mediaCount = mediaCount;
  }

}
