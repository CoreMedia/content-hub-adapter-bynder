package com.coremedia.labs.contenthub.adapters.bynder.service.model;

public abstract class SearchQuery {

  protected String term;
  protected int id = -1;

  public String getTerm() {
    return term;
  }

  public int getId() {
    return id;
  }

  public abstract String getType();
}
