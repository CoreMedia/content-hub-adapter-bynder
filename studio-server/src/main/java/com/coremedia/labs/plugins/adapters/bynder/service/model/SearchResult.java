package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> {

  @JsonProperty("total")
  private Total total;

  @JsonProperty("media")
  private List<T> media;

  public SearchResult() {
    this.media = new ArrayList<>();
  }

  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  public List<T> getMedia() {
    return media;
  }

  public void setMedia(List<T> media) {
    this.media = media;
  }

}
