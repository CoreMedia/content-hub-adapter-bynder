package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.*;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;

public abstract class BynderItem extends BynderContentHubObject implements Item {

  private static final Logger LOG = LoggerFactory.getLogger(BynderItem.class);

  private final BynderContentHubType type;

  public BynderItem(@NonNull ContentHubObjectId objectId, BynderContentHubType type) {
    super(objectId);
    this.type = type;
  }

  @Override
  public ContentHubType getContentHubType() {
    return type.getType();
  }

  public String getThumbnailUrl() {
    return null;
  }

  public String getDataUrl() {
    return null;
  }

  public abstract String getCopyright();

  @Nullable
  @Override
  public ContentHubBlob getBlob(String classifier) {
    ContentHubBlob blob = null;
    String blobUrl = getDataUrl();

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
