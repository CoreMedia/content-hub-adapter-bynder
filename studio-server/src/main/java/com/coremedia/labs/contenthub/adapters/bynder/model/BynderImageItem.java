package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubBlob;
import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.contenthub.api.UrlBlobBuilder;
import com.coremedia.contenthub.api.preview.DetailsElement;
import com.coremedia.contenthub.api.preview.DetailsSection;
import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Image;
import com.coremedia.mimetype.MimeTypeService;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class BynderImageItem extends BynderItem {

  private final Image image;

  public BynderImageItem(@NonNull ContentHubObjectId objectId,
                         @NonNull Image image,
                         @NonNull BynderService bynderService,
                         @NonNull MimeTypeService mimeTypeService) {
    super(objectId, image, BynderContentHubType.IMAGE, bynderService, mimeTypeService);
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
    // this URL will *not* be used in Content Hub Preview (see https://documentation.coremedia.com/cmcc-10/artifacts/2107/webhelp/studio-developer-en/content/Content_Hub.html)
    // correct preview delivery must be implemented through BynderItem#getBlob(String)
    String previewUrl = getPreviewUrl();
    if (StringUtils.isNotBlank(previewUrl)) {
      blob = new UrlBlobBuilder(this, CLASSIFIER_PREVIEW).withUrl(previewUrl).build();
    }

    return List.of(
            // Details
            new DetailsSection("main", List.of(
                    new DetailsElement<>(getName(), false, Objects.requireNonNullElse(blob, SHOW_TYPE_ICON)),
                    new DetailsElement<>("copyright", image.getCopyright()),
                    new DetailsElement<>("description", image.getDescription())
            ), false, false, false),

            // Metadata
            new DetailsSection("metadata", List.of(
                    new DetailsElement<>("id", image.getId()),
                    new DetailsElement<>("extension", image.getExtension() != null ? String.join(", ", image.getExtension()) : "-"),
                    new DetailsElement<>("dimensions", String.format("%dx%d", image.getWidth(), image.getHeight())),
                    new DetailsElement<>("size", FileUtils.byteCountToDisplaySize(image.getFileSize())),
                    new DetailsElement<>("user", image.getUserCreated()),
                    new DetailsElement<>("tags", image.getTags() != null ? String.join(", ", image.getTags()) : "-"),
                    new DetailsElement<>("dateCreated", getDateFormatted(image.getDateCreated())),
                    new DetailsElement<>("dateModified", getDateFormatted(image.getDateModified())),
                    new DetailsElement<>("datePublished", getDateFormatted(image.getDatePublished()))
            ))
    );
  }
}
