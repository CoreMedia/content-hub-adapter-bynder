package com.coremedia.labs.plugins.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Collection;
import edu.umd.cs.findbugs.annotations.NonNull;

public class BynderCollectionFolder extends BynderFolder {

  private Collection collection;

  public BynderCollectionFolder(@NonNull ContentHubObjectId objectId, Collection collection) {
    super(objectId, collection.getName(), BynderContentHubType.COLLECTION_FOLDER);
    this.collection = collection;
  }

  public Collection getCollection() {
    return collection;
  }

}
