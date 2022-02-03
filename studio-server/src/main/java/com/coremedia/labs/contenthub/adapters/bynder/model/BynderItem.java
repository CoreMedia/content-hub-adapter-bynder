package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.*;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Entity;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;

public abstract class BynderItem extends BynderContentHubObject implements Item {

  protected static final String CLASSIFIER_PREVIEW = "preview";

  private static final Logger LOG = LoggerFactory.getLogger(BynderItem.class);

  private final Entity entity;
  private final BynderContentHubType type;

  public BynderItem(@NonNull ContentHubObjectId objectId, Entity entity, BynderContentHubType type) {
    super(objectId);
    this.entity = entity;
    this.type = type;
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

  public abstract String getDataUrl();

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
    String blobUrl;
    if (classifier.equals(CLASSIFIER_PREVIEW)) {
      blobUrl = getPreviewUrl();
    } else {
      blobUrl = getDataUrl();
    }

    if (StringUtils.isNotBlank(blobUrl)) {
      try {
        blob = new UrlBlobBuilder(this, classifier).withUrl(blobUrl).build();
      } catch (Exception e) {
        throw new IllegalArgumentException("Cannot create blob for " + this, e);
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
