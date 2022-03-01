package com.coremedia.labs.plugins.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.plugins.adapters.bynder.service.BynderService;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Document;
import com.coremedia.mimetype.MimeTypeService;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Model representation of Bynder asset type "document". Not yet fully implemented.
 */
public class BynderDocumentItem extends BynderItem {

  private final Document document;

  public BynderDocumentItem(@NonNull ContentHubObjectId objectId,
                            @NonNull Document document,
                            @NonNull BynderService bynderService,
                            @NonNull MimeTypeService mimeTypeService) {
    super(objectId, document, BynderContentHubType.DOCUMENT, bynderService, mimeTypeService);
    this.document = document;
  }

  @Override
  public String getCoreMediaContentType() {
    return "CMDownload";
  }
}
