package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubBlob;
import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.contenthub.api.UrlBlobBuilder;
import com.coremedia.contenthub.api.preview.DetailsElement;
import com.coremedia.contenthub.api.preview.DetailsSection;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Photo;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class BynderPhotoItem extends BynderItem {

  private final Photo photo;

  public BynderPhotoItem(@NonNull ContentHubObjectId objectId, @NonNull Photo photo) {
    super(objectId, BynderContentHubType.IMAGE);
    this.photo = photo;
  }

  @Override
  public String getCoreMediaContentType() {
    return "CMPicture";
  }

  @Override
  public String getName() {
    return photo.getName();
  }

  @Nullable
  @Override
  public String getDescription() {
    return photo.getDescription();
  }

  @NonNull
  @Override
  public List<DetailsSection> getDetails() {
    ContentHubBlob blob = null;
    String thumbnailUrl = getThumbnailUrl();
    if (StringUtils.isNotBlank(thumbnailUrl)) {
      blob = new UrlBlobBuilder(this, "preview").withUrl(thumbnailUrl).build();
    }

    return List.of(
            // Details
            new DetailsSection("main", List.of(
                    new DetailsElement<>(getName(), false, Objects.requireNonNullElse(blob, SHOW_TYPE_ICON))
            ), false, false, false),

            // Metadata
            new DetailsSection("metadata", List.of(
                    new DetailsElement<>("id", photo.getId()),
                    new DetailsElement<>("dimensions", String.format("%dx%d", photo.getImageWidth(), photo.getImageHeight())),
                    new DetailsElement<>("size", FileUtils.byteCountToDisplaySize(photo.getImageSize())),
                    new DetailsElement<>("user", photo.getUser())
            ))
    );
  }

  @Override
  public String getThumbnailUrl() {
    return getPhoto().getPreviewUrl();
  }

  @Override
  public String getDataUrl() {
    return getPhoto().getLargeImageUrl();
  }

  @Override
  public String getCopyright() {
    return photo.getCopyright();
  }

  public Photo getPhoto() {
    return photo;
  }
}
