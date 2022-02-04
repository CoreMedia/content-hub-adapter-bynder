package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubBlob;
import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.contenthub.api.UrlBlobBuilder;
import com.coremedia.contenthub.api.preview.DetailsElement;
import com.coremedia.contenthub.api.preview.DetailsSection;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Video;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class BynderVideoItem extends BynderItem {

  private final Video video;

  public BynderVideoItem(@NonNull ContentHubObjectId objectId, @NonNull Video video) {
    super(objectId, video, BynderContentHubType.VIDEO);
    this.video = video;
  }

  @Override
  public String getCoreMediaContentType() {
    return "CMVideo";
  }

  @Override
  public String getName() {
    return video.getName();
  }

  @Nullable
  @Override
  public String getDescription() {
    return video.getDescription();
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
                    new DetailsElement<>("copyright", video.getCopyright()),
                    new DetailsElement<>("description", video.getDescription())
            ), false, false, false),

            // Metadata
            new DetailsSection("metadata", List.of(
                    new DetailsElement<>("id", video.getId()),
                    new DetailsElement<>("extension", video.getExtension() != null ? String.join(", ", video.getExtension()) : "-"),
                    new DetailsElement<>("size", FileUtils.byteCountToDisplaySize(video.getFileSize())),
                    new DetailsElement<>("user", video.getUserCreated()),
                    new DetailsElement<>("tags", video.getTags() != null ? String.join(", ", video.getTags()) : "-"),
                    new DetailsElement<>("dateCreated", getDateFormatted(video.getDateCreated())),
                    new DetailsElement<>("dateModified", getDateFormatted(video.getDateModified())),
                    new DetailsElement<>("datePublished", getDateFormatted(video.getDatePublished()))
            ))
    );
  }

  @Override
  public String getDataUrl() {
    return video.getVideoPreviewURL();
  }
}
