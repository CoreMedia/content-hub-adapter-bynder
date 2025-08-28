package com.coremedia.labs.plugins.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Tag;
import edu.umd.cs.findbugs.annotations.NonNull;

public class BynderTagFolder extends BynderFolder {

  private Tag tag;

  public BynderTagFolder(@NonNull ContentHubObjectId objectId, Tag tag) {
    super(objectId, tag.getTag(), BynderContentHubType.TAG_FOLDER);
    this.tag = tag;
  }

  public Tag getTag() {
    return tag;
  }

}
