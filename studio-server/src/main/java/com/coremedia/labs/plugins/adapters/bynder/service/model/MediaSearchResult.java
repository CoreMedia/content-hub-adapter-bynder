package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class MediaSearchResult<T> {

  @JsonProperty("count") private Count count;
  @JsonProperty("media") private List<T> media;

  public Count getCount() {
    return count;
  }

  public void setCount(Count count) {
    this.count = count;
  }

  public List<T> getMedia() {
    return media;
  }

  public void setMedia(List<T> media) {
    this.media = media;
  }

  public static class Count {
    @JsonProperty("brandId") private Map<String, Integer> brandId;
    @JsonProperty("categoryId") private Map<String, Integer> categoryId;
    @JsonProperty("orientation") private Map<String, Integer> orientation;
    @JsonProperty("subBrandId") private Map<String, Integer> subBrandId;
    @JsonProperty("tags") private Map<String, Integer> tags;
    @JsonProperty("total") private int total;
    @JsonProperty("type") private Map<String, Integer> type;

    public Map<String, Integer> getBrandId() {
      return brandId;
    }

    public void setBrandId(Map<String, Integer> brandId) {
      this.brandId = brandId;
    }

    public Map<String, Integer> getCategoryId() {
      return categoryId;
    }

    public void setCategoryId(Map<String, Integer> categoryId) {
      this.categoryId = categoryId;
    }

    public Map<String, Integer> getOrientation() {
      return orientation;
    }

    public void setOrientation(Map<String, Integer> orientation) {
      this.orientation = orientation;
    }

    public Map<String, Integer> getSubBrandId() {
      return subBrandId;
    }

    public void setSubBrandId(Map<String, Integer> subBrandId) {
      this.subBrandId = subBrandId;
    }

    public Map<String, Integer> getTags() {
      return tags;
    }

    public void setTags(Map<String, Integer> tags) {
      this.tags = tags;
    }

    public int getTotal() {
      return total;
    }

    public void setTotal(int total) {
      this.total = total;
    }

    public Map<String, Integer> getType() {
      return type;
    }

    public void setType(Map<String, Integer> type) {
      this.type = type;
    }
  }

}
