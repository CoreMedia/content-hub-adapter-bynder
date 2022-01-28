package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Entity {

  @JsonProperty("id")
  private int id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("user")
  private String user;

  @JsonProperty("copyright")
  private String copyright;

  @JsonProperty("description")
  private String description;

  public int getId() {
    return id;
  }

  public void setId(int id) {
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
}
