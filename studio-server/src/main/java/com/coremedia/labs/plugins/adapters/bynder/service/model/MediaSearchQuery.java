package com.coremedia.labs.plugins.adapters.bynder.service.model;

import com.coremedia.contenthub.api.ContentHubType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaSearchQuery {

  private static final Logger LOG = LoggerFactory.getLogger(MediaSearchQuery.class);

  protected String term;
  protected String id;
  protected ContentHubType type;
  protected int limit;
  protected String collectionId;

  public static MediaSearchQuery queryForTerm(String term) {
    MediaSearchQuery mediaSearchQuery = new MediaSearchQuery();
    mediaSearchQuery.term = term;
    return mediaSearchQuery;
  }

  public static MediaSearchQuery queryForId(String id) {
    MediaSearchQuery mediaSearchQuery = new MediaSearchQuery();
    mediaSearchQuery.id = id;
    return mediaSearchQuery;
  }

  public static MediaSearchQuery fromQuery(MediaSearchQuery originalQuery) {
    try {
      return originalQuery.clone();
    } catch (Exception e) {
      // logging already performed in #clone()
      return null;
    }
  }

  public MediaSearchQuery withTerm(String term) {
    this.term = term;
    this.id = null;
    return this;
  }

  public MediaSearchQuery withId(String id) {
    this.id = id;
    this.term = null;
    return this;
  }

  public MediaSearchQuery withType(ContentHubType type) {
    this.type = type;
    return this;
  }

  public MediaSearchQuery withLimit(int limit) {
    this.limit = limit;
    return this;
  }

  public MediaSearchQuery inCollection(String collectionId) {
    this.collectionId = collectionId;
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

  public int getLimit() {
    return limit;
  }

  public String getCollectionId() {
    return collectionId;
  }

  @Override
  public MediaSearchQuery clone() throws CloneNotSupportedException {
    try {
      MediaSearchQuery clone = (MediaSearchQuery) super.clone();
      clone.term = getTerm();
      clone.id = getId();
      clone.type = getType();
      clone.limit = getLimit();
      clone.collectionId = getCollectionId();
      return clone;
    } catch (Exception e) {
      LOG.error("cannot clone search query" + this, e);
      throw new CloneNotSupportedException("cannot clone search query " + this);
    }
  }
}
