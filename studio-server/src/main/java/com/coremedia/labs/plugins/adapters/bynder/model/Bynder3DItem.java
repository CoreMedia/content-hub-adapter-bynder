package com.coremedia.labs.plugins.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.plugins.adapters.bynder.service.BynderService;
import com.coremedia.labs.plugins.adapters.bynder.service.model.ThreeD;
import com.coremedia.mimetype.MimeTypeService;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Model representation of Bynder asset type "3d". Not yet fully implemented.
 */
public class Bynder3DItem extends BynderItem {

  private final ThreeD threeD;

  public Bynder3DItem(@NonNull ContentHubObjectId objectId,
                      @NonNull ThreeD threeD,
                      @NonNull BynderService bynderService,
                      @NonNull MimeTypeService mimeTypeService) {
    super(objectId, threeD, BynderContentHubType.THREE_D, bynderService, mimeTypeService);
    this.threeD = threeD;
  }

  @Override
  public String getCoreMediaContentType() {
    return "CMDownload";
  }
}
