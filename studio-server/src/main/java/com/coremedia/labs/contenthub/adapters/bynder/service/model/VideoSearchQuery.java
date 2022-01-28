package com.coremedia.labs.contenthub.adapters.bynder.service.model;

public class VideoSearchQuery extends SearchQuery {

  public static VideoSearchQuery queryForTerm(String term) {
    VideoSearchQuery searchQuery = new VideoSearchQuery();
    searchQuery.term = term;
    return searchQuery;
  }

  public static VideoSearchQuery queryForId(int id) {
    VideoSearchQuery searchQuery = new VideoSearchQuery();
    searchQuery.id = id;
    return searchQuery;
  }

  public static VideoSearchQuery fromQuery(VideoSearchQuery originalQuery) {
    VideoSearchQuery searchQuery = new VideoSearchQuery();
    searchQuery.term = originalQuery.getTerm();
    searchQuery.id = originalQuery.getId();
    searchQuery.isEditorsChoice = originalQuery.isEditorsChoice();
    searchQuery.isSafeSearch = originalQuery.isSafeSearch();
    searchQuery.minWidth = originalQuery.getMinWidth();
    searchQuery.minHeight = originalQuery.getMinHeight();
    return searchQuery;
  }

  public VideoSearchQuery withTerm(String term) {
    this.term = term;
    this.id = -1;
    return this;
  }

  public VideoSearchQuery withId(int id) {
    this.id = id;
    this.term = null;
    return this;
  }

  public VideoSearchQuery withMinWidth(int minWidth) {
    assert minWidth >= 0;
    this.minWidth = minWidth;
    return this;
  }

  public VideoSearchQuery withMinHeight(int minHeight) {
    assert minHeight >= 0;
    this.minHeight = minHeight;
    return this;
  }

  public VideoSearchQuery withEditorsChoice(boolean value) {
    this.isEditorsChoice = value;
    return this;
  }

  public VideoSearchQuery withSafeSearch(boolean value) {
    this.isSafeSearch = value;
    return this;
  }
}
