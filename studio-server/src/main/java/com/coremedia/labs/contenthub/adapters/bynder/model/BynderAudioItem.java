package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Audio;
import com.coremedia.mimetype.MimeTypeService;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Model representation of Bynder asset type "audio". Not yet fully implemented.
 */
public class BynderAudioItem extends BynderItem {

  private final Audio audio;

  public BynderAudioItem(@NonNull ContentHubObjectId objectId,
                         @NonNull Audio audio,
                         @NonNull BynderService bynderService,
                         @NonNull MimeTypeService mimeTypeService) {
    super(objectId, audio, BynderContentHubType.AUDIO, bynderService, mimeTypeService);
    this.audio = audio;
  }

  @Override
  public String getCoreMediaContentType() {
    return "CMAudio";
  }
}
