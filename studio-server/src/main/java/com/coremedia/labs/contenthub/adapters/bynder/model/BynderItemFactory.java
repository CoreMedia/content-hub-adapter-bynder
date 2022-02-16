package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.*;
import com.coremedia.mimetype.MimeTypeService;

/**
 * Factory to create Content Hub Objects from Bynder assets.
 */
public class BynderItemFactory {

  // Static class
  private BynderItemFactory() {
  }

  public static BynderItem createItem(ContentHubObjectId id,
                                      Entity asset,
                                      BynderService bynderService,
                                      MimeTypeService mimeTypeService) {
    if (asset instanceof Image) {
      return new BynderImageItem(id, (Image) asset, bynderService, mimeTypeService);
    } else if (asset instanceof Video) {
      return new BynderVideoItem(id, (Video) asset, bynderService, mimeTypeService);
    } else if (asset instanceof Document) {
      return new BynderDocumentItem(id, (Document) asset, bynderService, mimeTypeService);
    } else if (asset instanceof Audio) {
      return new BynderAudioItem(id, (Audio) asset, bynderService, mimeTypeService);
    } else if (asset instanceof ThreeD) {
      return new Bynder3DItem(id, (ThreeD) asset, bynderService, mimeTypeService);
    } else {
      throw new IllegalArgumentException("cannot create Bynder item for content hub object id " + id.asString() +
              ", unknown asset type " + asset.getClass().getName());
    }
  }
}
