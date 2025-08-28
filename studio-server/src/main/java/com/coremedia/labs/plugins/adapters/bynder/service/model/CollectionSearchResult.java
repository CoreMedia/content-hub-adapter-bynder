package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CollectionSearchResult {

  @JsonProperty("count")
  private int count;

  @JsonProperty("collections")
  private List<Collection> collections;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<Collection> getCollections() {
    return collections;
  }

  public void setCollections(List<Collection> collections) {
    this.collections = collections;
  }

}
