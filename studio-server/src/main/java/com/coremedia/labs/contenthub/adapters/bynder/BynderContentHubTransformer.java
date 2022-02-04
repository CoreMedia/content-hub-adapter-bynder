package com.coremedia.labs.contenthub.adapters.bynder;

import com.coremedia.contenthub.api.*;
import com.coremedia.labs.contenthub.adapters.bynder.model.BynderImageItem;
import com.coremedia.labs.contenthub.adapters.bynder.model.BynderItem;
import com.coremedia.labs.contenthub.adapters.bynder.model.BynderVideoItem;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class BynderContentHubTransformer implements ContentHubTransformer {

  private static final Logger LOG = LoggerFactory.getLogger(BynderContentHubTransformer.class);

  @Nullable
  @Override
  public ContentModel transform(Item source, ContentHubAdapter contentHubAdapter, ContentHubContext contentHubContext) {
    if (!(source instanceof BynderItem)) {
      throw new IllegalArgumentException("Cannot transform source " + source);
    }

    BynderItem item = (BynderItem) source;
    LOG.info("Creating content model for item {}.", item);

    ContentModel contentModel = ContentModel.createContentModel(item);
    contentModel.put("title", item.getName());
    contentModel.put("copyright", item.getCopyright());
    String description = item.getDescription();
    if (description != null) {
      contentModel.put("detailText", ContentCreationUtil.convertStringToRichtext(description));
    }

    if (item instanceof BynderImageItem) {
      ContentHubBlob blob = item.getBlob("file");
      if (blob != null) {
        contentModel.put("data", blob);
      }
    } else if (item instanceof BynderVideoItem) {
      BynderVideoItem videoItem = (BynderVideoItem) item;
      contentModel.put("dataUrl", videoItem.getDataUrl());

      // Store preview still image reference
      String previewUrl = videoItem.getPreviewUrl();
      if (previewUrl != null) {
        ContentModelReference ref = ContentModelReference.create(contentModel, "CMPicture", previewUrl);
        contentModel.put("pictures", Collections.singletonList(ref));
      }
    }

    return contentModel;
  }

  @Nullable
  @Override
  public ContentModel resolveReference(ContentHubObject owner, ContentModelReference reference, ContentHubAdapter contentHubAdapter, ContentHubContext contentHubContext) {
    Object data = reference.getData();
    if (!(data instanceof String)) {
      throw new IllegalArgumentException("no valid reference: " + reference);
    }

    String imageUrl = (String) data;
    String imageName = reference.getOwner().getContentName() + " (Preview)";

    ContentModel referenceModel = ContentModel.createReferenceModel(imageName, reference.getCoreMediaContentType());
    referenceModel.put("data", new UrlBlobBuilder(owner, "preview").withUrl(imageUrl).build());
    referenceModel.put("title", "Video " + imageName);

    return referenceModel;
  }
}
