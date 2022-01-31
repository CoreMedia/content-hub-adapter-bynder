package com.coremedia.labs.contenthub.adapters.bynder.service.model;

public class ImageSearchQuery extends SearchQuery {

  private Image.Orientation orientation;

  public static ImageSearchQuery queryForTerm(String term) {
    ImageSearchQuery searchQuery = new ImageSearchQuery();
    searchQuery.term = term;
    return searchQuery;
  }

  public static ImageSearchQuery queryForId(int id) {
    ImageSearchQuery searchQuery = new ImageSearchQuery();
    searchQuery.id = id;
    return searchQuery;
  }

  public static ImageSearchQuery fromQuery(ImageSearchQuery originalQuery) {
    ImageSearchQuery searchQuery = new ImageSearchQuery();
    searchQuery.term = originalQuery.getTerm();
    searchQuery.id = originalQuery.getId();
    return searchQuery;
  }

  public ImageSearchQuery withTerm(String term) {
    this.term = term;
    this.id = -1;
    return this;
  }

  public ImageSearchQuery withId(int id) {
    this.id = id;
    this.term = null;
    return this;
  }

  public ImageSearchQuery withOrientation(Image.Orientation orientation) {
    this.orientation = orientation;
    return this;
  }

  public Image.Orientation getOrientation() {
    return orientation;
  }

  public void setOrientation(Image.Orientation orientation) {
    this.orientation = orientation;
  }

  public String getType() {
    return Image.TYPE;
  }
}
