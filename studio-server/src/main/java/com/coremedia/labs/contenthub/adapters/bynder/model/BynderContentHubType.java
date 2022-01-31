package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubType;

public enum BynderContentHubType {

  FOLDER(new ContentHubType("Folder")),
  IMAGE(new ContentHubType("Image")),
  VIDEO(new ContentHubType("Video"));

  private final ContentHubType type;

  BynderContentHubType(ContentHubType type) {
    this.type = type;
  }

  public ContentHubType getType() {
    return type;
  }
}
