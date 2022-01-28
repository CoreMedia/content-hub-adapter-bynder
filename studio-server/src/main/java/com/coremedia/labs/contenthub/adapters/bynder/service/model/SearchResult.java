package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<T> {

  @JsonProperty("total")
  private int total;

  @JsonProperty("totalHits")
  private int totalHits;

  @JsonProperty("hits")
  private List<T> hits;

  public SearchResult() {
    this.hits = new ArrayList<>();
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getTotalHits() {
    return totalHits;
  }

  public void setTotalHits(int totalHits) {
    this.totalHits = totalHits;
  }

  public List<T> getHits() {
    return hits;
  }

  public void setHits(List<T> hits) {
    this.hits = hits;
  }

}
