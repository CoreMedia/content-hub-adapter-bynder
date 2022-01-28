package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Video extends Entity {

  public enum VideoType {
    all, film, animation
  }

  @JsonProperty("type")
  private VideoType type;

  @JsonProperty("duration")
  private int duration;

  @JsonProperty("picture_id")
  private String pictureId;

  @JsonProperty("videos")
  private VideoLinks videos;

  public VideoType getType() {
    return type;
  }

  public void setType(VideoType type) {
    this.type = type;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getPictureId() {
    return pictureId;
  }

  public void setPictureId(String pictureId) {
    this.pictureId = pictureId;
  }

  public VideoLinks getVideos() {
    return videos;
  }

  public void setVideos(VideoLinks videos) {
    this.videos = videos;
  }
}
