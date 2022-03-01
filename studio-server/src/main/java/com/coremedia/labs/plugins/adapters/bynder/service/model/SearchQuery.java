package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.coremedia.contenthub.api.ContentHubType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchQuery {

  private static final Logger LOG = LoggerFactory.getLogger(SearchQuery.class);

  protected String term;
  protected String id;
  protected ContentHubType type;

  public static SearchQuery queryForTerm(String term) {
    SearchQuery searchQuery = new SearchQuery();
    searchQuery.term = term;
    return searchQuery;
  }

  public static SearchQuery queryForId(String id) {
    SearchQuery searchQuery = new SearchQuery();
    searchQuery.id = id;
    return searchQuery;
  }

  public static SearchQuery fromQuery(SearchQuery originalQuery) {
    try {
      return originalQuery.clone();
    } catch (Exception e) {
      // logging already performed in #clone()
      return null;
    }
  }

  public SearchQuery withTerm(String term) {
    this.term = term;
    this.id = null;
    return this;
  }

  public SearchQuery withId(String id) {
    this.id = id;
    this.term = null;
    return this;
  }

  public SearchQuery withType(ContentHubType type) {
    this.type = type;
    return this;
  }

  public String getTerm() {
    return term;
  }

  public String getId() {
    return id;
  }

  public ContentHubType getType() {
    return type;
  }

  @Override
  public SearchQuery clone() throws CloneNotSupportedException {
    try {
      SearchQuery clone = (SearchQuery) super.clone();
      clone.term = getTerm();
      clone.id = getId();
      clone.type = getType();
      return clone;
    } catch (Exception e) {
      LOG.error("cannot clone search query" + this, e);
      throw new CloneNotSupportedException("cannot clone search query " + this);
    }
  }
}
