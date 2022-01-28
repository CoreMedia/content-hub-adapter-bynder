package com.coremedia.labs.contenthub.adapters.bynder.service.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoSearchQuery extends SearchQuery {

  private Photo.Orientation orientation;

  public static PhotoSearchQuery queryForTerm(String term) {
    PhotoSearchQuery searchQuery = new PhotoSearchQuery();
    searchQuery.term = term;
    return searchQuery;
  }

  public static PhotoSearchQuery queryForId(int id) {
    PhotoSearchQuery searchQuery = new PhotoSearchQuery();
    searchQuery.id = id;
    return searchQuery;
  }

  public static PhotoSearchQuery fromQuery(PhotoSearchQuery originalQuery) {
    PhotoSearchQuery searchQuery = new PhotoSearchQuery();
    searchQuery.term = originalQuery.getTerm();
    searchQuery.id = originalQuery.getId();
    searchQuery.isSafeSearch = originalQuery.isSafeSearch();
    searchQuery.minWidth = originalQuery.getMinWidth();
    searchQuery.minHeight = originalQuery.getMinHeight();
    return searchQuery;
  }

  public PhotoSearchQuery withTerm(String term) {
    this.term = term;
    this.id = -1;
    return this;
  }

  public PhotoSearchQuery withId(int id) {
    this.id = id;
    this.term = null;
    return this;
  }

  public PhotoSearchQuery withMinWidth(int minWidth) {
    assert minWidth >= 0;
    this.minWidth = minWidth;
    return this;
  }

  public PhotoSearchQuery withMinHeight(int minHeight) {
    assert minHeight >= 0;
    this.minHeight = minHeight;
    return this;
  }

  public PhotoSearchQuery withEditorsChoice(boolean value) {
    this.isEditorsChoice = value;
    return this;
  }

  public PhotoSearchQuery withSafeSearch(boolean value) {
    this.isSafeSearch = value;
    return this;
  }

  public PhotoSearchQuery withOrientation(Photo.Orientation orientation) {
    this.orientation = orientation;
    return this;
  }

  public Photo.Orientation getOrientation() {
    return orientation;
  }

  public void setOrientation(Photo.Orientation orientation) {
    this.orientation = orientation;
  }
}
