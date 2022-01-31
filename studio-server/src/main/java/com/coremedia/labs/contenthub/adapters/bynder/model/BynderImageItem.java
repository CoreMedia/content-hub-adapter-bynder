package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubBlob;
import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.contenthub.api.UrlBlobBuilder;
import com.coremedia.contenthub.api.preview.DetailsElement;
import com.coremedia.contenthub.api.preview.DetailsSection;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Image;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class BynderImageItem extends BynderItem {

  private final Image image;

  public BynderImageItem(@NonNull ContentHubObjectId objectId, @NonNull Image image) {
    super(objectId, BynderContentHubType.IMAGE);
    this.image = image;
  }

  @Override
  public String getCoreMediaContentType() {
    return "CMPicture";
  }

  @Override
  public String getName() {
    return image.getName();
  }

  @Nullable
  @Override
  public String getDescription() {
    return image.getDescription();
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
                    new DetailsElement<>("id", image.getId()),
                    new DetailsElement<>("dimensions", String.format("%dx%d", image.getWidth(), image.getHeight())),
                    new DetailsElement<>("size", FileUtils.byteCountToDisplaySize(image.getFileSize())),
                    new DetailsElement<>("user", image.getUser())
            ))
    );
  }

  @Override
  public String getThumbnailUrl() {
    return getPhoto().getThumbnails().getUrl();
  }

  @Override
  public String getDataUrl() {
    return getPhoto().getTransformBaseUrl();
  }

  @Override
  public String getCopyright() {
    return image.getCopyright();
  }

  public Image getPhoto() {
    return image;
  }
}
