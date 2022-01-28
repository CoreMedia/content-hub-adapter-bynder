package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoLinks {

  @JsonProperty("large")
  private VideoRef large;

  @JsonProperty("medium")
  private VideoRef medium;

  @JsonProperty("small")
  private VideoRef small;

  @JsonProperty("tiny")
  private VideoRef tiny;

  public VideoRef getLarge() {
    return large;
  }

  public void setLarge(VideoRef large) {
    this.large = large;
  }

  public VideoRef getMedium() {
    return medium;
  }

  public void setMedium(VideoRef medium) {
    this.medium = medium;
  }

  public VideoRef getSmall() {
    return small;
  }

  public void setSmall(VideoRef small) {
    this.small = small;
  }

  public VideoRef getTiny() {
    return tiny;
  }

  public void setTiny(VideoRef tiny) {
    this.tiny = tiny;
  }
}
