package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubType;

public enum BynderContentHubType {

  ALL(new ContentHubType("all")),
  FOLDER(new ContentHubType("folder")),
  IMAGE(new ContentHubType("image")),
  VIDEO(new ContentHubType("video"));

  private final ContentHubType type;

  BynderContentHubType(ContentHubType type) {
    this.type = type;
  }

  public ContentHubType getType() {
    return type;
  }
}
