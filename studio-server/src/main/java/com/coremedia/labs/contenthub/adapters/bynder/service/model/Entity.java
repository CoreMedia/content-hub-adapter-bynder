package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Image.class, name = Image.TYPE),
        @JsonSubTypes.Type(value = Video.class, name = Video.TYPE)
})
public abstract class Entity {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("user")
  private String user;

  @JsonProperty("copyright")
  private String copyright;

  @JsonProperty("description")
  private String description;

  @JsonProperty("thumbnails")
  private Thumbnails thumbnails;

  @JsonProperty("fileSize")
  private int fileSize;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public Thumbnails getThumbnails() {
    return thumbnails;
  }

  public void setThumbnails(Thumbnails thumbnails) {
    this.thumbnails = thumbnails;
  }

  public int getFileSize() {
    return fileSize;
  }

  public void setFileSize(int fileSize) {
    this.fileSize = fileSize;
  }
}
