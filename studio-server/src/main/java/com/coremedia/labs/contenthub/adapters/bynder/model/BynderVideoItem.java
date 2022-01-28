package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubBlob;
import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.contenthub.api.UrlBlobBuilder;
import com.coremedia.contenthub.api.preview.DetailsElement;
import com.coremedia.contenthub.api.preview.DetailsSection;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Video;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class BynderVideoItem extends BynderItem {

  private final Video video;

  public BynderVideoItem(@NonNull ContentHubObjectId objectId, @NonNull Video video) {
    super(objectId, BynderContentHubType.VIDEO);
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
    String thumbnailUrl = getThumbnailUrl();
    if (StringUtils.isNotBlank(thumbnailUrl)) {
      blob = new UrlBlobBuilder(this, "preview").withUrl(thumbnailUrl).build();
    }

    Duration videoDuration = Duration.ofSeconds(getVideo().getDuration());

    return List.of(
            // Details
            new DetailsSection("main", List.of(
                    new DetailsElement<>(getName(), false, Objects.requireNonNullElse(blob, SHOW_TYPE_ICON))
            ), false, false, false),

            // Metadata
            new DetailsSection("metadata", List.of(
                    new DetailsElement<>("id", video.getId()),
                    new DetailsElement<>("type", getVideo().getType()),
                    new DetailsElement<>("duration", DurationFormatUtils.formatDuration(videoDuration.toMillis(), "H:mm:ss", true)),
                    new DetailsElement<>("user", video.getUser())
            ))
    );
  }

  @Override
  public String getThumbnailUrl() {
    return String.format("https://i.vimeocdn.com/video/%s_640x360.jpg", video.getPictureId());
  }

  @Override
  public String getDataUrl() {
    return video.getVideos().getLarge().getUrl();
  }

  @Override
  public String getCopyright() {
    return video.getCopyright();
  }

  public Video getVideo() {
    return video;
  }

}
