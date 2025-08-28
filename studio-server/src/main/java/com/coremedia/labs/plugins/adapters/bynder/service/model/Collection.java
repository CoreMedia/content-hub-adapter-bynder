package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Collection {

  @JsonProperty("collectionCount") private int collectionCount;
  @JsonProperty("cover") private Cover cover;
  @JsonProperty("dateCreated") private String dateCreated;
  @JsonProperty("dateModified") private String dateModified;
  @JsonProperty("description") private String description;
  @JsonProperty("filename") private String filename;
  @JsonProperty("id") private String id;
  @JsonProperty("isEmbeddable") private int isEmbeddable;
  @JsonProperty("IsPublic") private String isPublic;
  @JsonProperty("link") private String link;
  @JsonProperty("name") private String name;
  @JsonProperty("userId") private String userId;

  public int getCollectionCount() {
    return collectionCount;
  }

  public void setCollectionCount(int collectionCount) {
    this.collectionCount = collectionCount;
  }

  public Cover getCover() {
    return cover;
  }

  public void setCover(Cover cover) {
    this.cover = cover;
  }

  public String getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getDateModified() {
    return dateModified;
  }

  public void setDateModified(String dateModified) {
    this.dateModified = dateModified;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getIsEmbeddable() {
    return isEmbeddable;
  }

  public void setIsEmbeddable(int isEmbeddable) {
    this.isEmbeddable = isEmbeddable;
  }

  public String getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(String isPublic) {
    this.isPublic = isPublic;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  static class Cover {
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("thumbnails")
    private List<String> thumbnails;
    @JsonProperty("large")
    private String large;

    public String getThumbnail() {
      return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
      this.thumbnail = thumbnail;
    }

    public List<String> getThumbnails() {
      return thumbnails;
    }

    public void setThumbnails(List<String> thumbnails) {
      this.thumbnails = thumbnails;
    }

    public String getLarge() {
      return large;
    }

    public void setLarge(String large) {
      this.large = large;
    }
  }

}
