package com.coremedia.labs.plugins.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.plugins.adapters.bynder.service.BynderService;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Audio;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Collection;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Document;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Entity;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Image;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Tag;
import com.coremedia.labs.plugins.adapters.bynder.service.model.ThreeD;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Video;
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

  public static BynderCollectionFolder createCollectionFolder(ContentHubObjectId id, Collection collection) {
    return new BynderCollectionFolder(id, collection);
  }

  public static BynderTagFolder createTagFolder(ContentHubObjectId id, Tag tag) {
    return new BynderTagFolder(id, tag);
  }

}
