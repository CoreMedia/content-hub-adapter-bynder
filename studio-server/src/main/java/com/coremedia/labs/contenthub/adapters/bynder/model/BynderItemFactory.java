package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Entity;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Image;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Video;

/**
 * Factory to create Content Hub Objects from Bynder assets.
 */
public class BynderItemFactory {

  // Static class
  private BynderItemFactory() {
  }

  public static BynderItem createItem(ContentHubObjectId id, Entity asset) {
    if (asset instanceof Image) {
      return new BynderImageItem(id, (Image) asset);
    } else if (asset instanceof Video) {
      return new BynderVideoItem(id, (Video) asset);
    } else {
      throw new IllegalArgumentException("cannot create Bynder item for content hub object id " + id.asString());
    }
  }
}
