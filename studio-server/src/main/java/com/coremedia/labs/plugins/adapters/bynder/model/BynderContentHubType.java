package com.coremedia.labs.plugins.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubType;

public enum BynderContentHubType {

  ALL(new ContentHubType("all")),
  FOLDER(new ContentHubType("folder")),
  PORTAL_FOLDER(new ContentHubType("portal")),
  COLLECTION_FOLDER(new ContentHubType("collection")),
  TAG_FOLDER(new ContentHubType("tag")),
  IMAGE(new ContentHubType("image")),
  VIDEO(new ContentHubType("video")),
  DOCUMENT(new ContentHubType("document")),
  AUDIO(new ContentHubType("audio")),
  THREE_D(new ContentHubType("3d"));

  private final ContentHubType type;

  BynderContentHubType(ContentHubType type) {
    this.type = type;
  }

  public ContentHubType getType() {
    return type;
  }
}
