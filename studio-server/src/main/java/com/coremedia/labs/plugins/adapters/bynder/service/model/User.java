package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

  @JsonProperty("id")
  private String id;
  @JsonProperty("name")
  private String name;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
