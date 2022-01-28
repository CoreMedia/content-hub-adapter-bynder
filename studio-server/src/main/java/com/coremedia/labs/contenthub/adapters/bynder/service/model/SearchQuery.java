package com.coremedia.labs.contenthub.adapters.bynder.service.model;

public abstract class SearchQuery {

  protected String term;
  protected int id = -1;
  protected int minWidth = 0;
  protected int minHeight = 0;
  protected boolean isEditorsChoice = false;
  protected boolean isSafeSearch = false;

  public String getTerm() {
    return term;
  }

  public int getId() {
    return id;
  }

  public int getMinWidth() {
    return minWidth;
  }

  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  public int getMinHeight() {
    return minHeight;
  }

  public void setMinHeight(int minHeight) {
    this.minHeight = minHeight;
  }

  public boolean isEditorsChoice() {
    return isEditorsChoice;
  }

  public void setEditorsChoice(boolean editorsChoice) {
    isEditorsChoice = editorsChoice;
  }

  public boolean isSafeSearch() {
    return isSafeSearch;
  }

  public void setSafeSearch(boolean safeSearch) {
    isSafeSearch = safeSearch;
  }
}
