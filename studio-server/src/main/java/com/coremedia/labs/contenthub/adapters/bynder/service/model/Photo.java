package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Photo extends Entity {

  @JsonProperty("thumbnails.mini") // TODO
  private String previewUrl;

  @JsonProperty("transformBaseUrl")
  private String largeImageUrl;

  @JsonProperty("width")
  private int imageWidth;

  @JsonProperty("height")
  private int imageHeight;

  @JsonProperty("fileSize")
  private int imageSize;

  public String getPreviewUrl() {
    return previewUrl;
  }

  public void setPreviewUrl(String previewUrl) {
    this.previewUrl = previewUrl;
  }

  public String getLargeImageUrl() {
    return largeImageUrl;
  }

  public void setLargeImageUrl(String largeImageUrl) {
    this.largeImageUrl = largeImageUrl;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
  }

  public int getImageSize() {
    return imageSize;
  }

  public void setImageSize(int imageSize) {
    this.imageSize = imageSize;
  }

  public enum Orientation {
    landscape, portrait, square
  }
}
