package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.*;
import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Entity;
import com.coremedia.mimetype.MimeTypeService;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimeType;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;

public abstract class BynderItem extends BynderContentHubObject implements Item {

  public static final String CLASSIFIER_PREVIEW = "preview";
  public static final String CLASSIFIER_ORIGINAL = "original";

  // buffer size for reading blob from remote server - needs to be large enough to cover what
  // org.apache.tika.mime.MimeTypes#detect(...) requires
  private static final int BUFFER_SIZE = 16384;
  private static final Logger LOG = LoggerFactory.getLogger(BynderItem.class);

  private final Entity entity;
  private final BynderContentHubType type;
  private final BynderService bynderService;
  private final MimeTypeService mimeTypeService;

  public BynderItem(@NonNull ContentHubObjectId objectId,
                    @NonNull Entity entity,
                    @NonNull BynderContentHubType type,
                    @NonNull BynderService bynderService,
                    @NonNull MimeTypeService mimeTypeService) {
    super(objectId);
    this.entity = entity;
    this.type = type;
    this.bynderService = bynderService;
    this.mimeTypeService = mimeTypeService;
  }

  @Override
  public ContentHubType getContentHubType() {
    return type.getType();
  }

  public String getThumbnailUrl() {
    return entity.getThumbnails().getMiniUrl();
  }

  public String getPreviewUrl() {
    return entity.getThumbnails().getWebImageUrl();
  }

  public String getDataUrl() {
    if (StringUtils.isNotBlank(entity.getOriginal())) {
      return entity.getOriginal();
    } else {
      return bynderService.getAssetDownloadById(entity.getId());
    }
  }

  public String getCopyright() {
    return entity.getCopyright();
  }

  public String getUserCreated() {
    return entity.getUserCreated();
  }

  public Date getDateCreated() {
    return entity.getDateCreated();
  }

  public Date getDateModified() {
    return entity.getDateModified();
  }

  @Nullable
  @Override
  public ContentHubBlob getBlob(String classifier) {
    ContentHubBlob blob = null;
    String blobUrl = null;
    if (classifier.equals(CLASSIFIER_PREVIEW)) {
      blobUrl = getPreviewUrl();
    } else if (classifier.equals(CLASSIFIER_ORIGINAL)) {
      blobUrl = getDataUrl();
    }

    if (StringUtils.isNotBlank(blobUrl)) {
      try {
        blob = new UrlBlobBuilder(this, classifier).withUrl(blobUrl).build();
        if (blob != null) {
          // Bynder's "download" API endpoint delivers an S3 URL which, when requested, won't have a fully qualified mime type
          // in response header (i.e., you will get "image/*" instead of "image/jpeg").
          // Determine mime type explicitly here since SQLBLobStore#guessMimeType(...) chokes on resetting the
          // input stream.
          final InputStream inputStream = new BufferedInputStream(blob.getInputStream(), BUFFER_SIZE);
          MimeType mimeType = new MimeType(mimeTypeService.detectMimeType(inputStream, blobUrl, blob.getContentType().toString()));
          inputStream.reset();
          blob = new ContentHubDefaultBlob(blob.getOwner(),
                  blob.getClassifier(),
                  mimeType,
                  blob.getLength(),
                  () -> inputStream,
                  blob.getEtag().orElse(null));
        }
      } catch (Exception e) {
        throw new IllegalArgumentException("cannot create blob for " + this, e);
      }
    }

    return blob;
  }

  protected String getDateFormatted(Date date) {
    try {
      return DateFormat.getInstance().format(date);
    } catch (Exception e) {
      LOG.error("cannot format date {}", date);
    }
    return "-";
  }
}
