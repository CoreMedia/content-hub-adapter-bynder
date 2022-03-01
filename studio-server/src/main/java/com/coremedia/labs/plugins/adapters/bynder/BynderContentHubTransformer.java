package com.coremedia.labs.plugins.adapters.bynder;

import com.coremedia.contenthub.api.*;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderImageItem;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderItem;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BynderContentHubTransformer implements ContentHubTransformer {

  private static final Logger LOG = LoggerFactory.getLogger(BynderContentHubTransformer.class);

  private static final String PROPERTY_TITLE = "title";
  private static final String PROPERTY_DATA = "data";
  private static final String PROPERTY_COPYRIGHT = "copyright";
  private static final String PROPERTY_DETAIL_TEXT = "detailText";

  @Nullable
  @Override
  public ContentModel transform(Item source, ContentHubAdapter contentHubAdapter, ContentHubContext contentHubContext) {
    if (!(source instanceof BynderItem)) {
      throw new IllegalArgumentException("cannot transform source " + source);
    }

    BynderItem item = (BynderItem) source;
    LOG.debug("creating content model for item {}", item);

    ContentModel contentModel = ContentModel.createContentModel(item);
    contentModel.put(PROPERTY_TITLE, item.getName());
    contentModel.put(PROPERTY_COPYRIGHT, item.getCopyright());
    String description = item.getDescription();
    if (description != null) {
      contentModel.put(PROPERTY_DETAIL_TEXT, ContentCreationUtil.convertStringToRichtext(description));
    }

    if (item instanceof BynderImageItem) {
      ContentHubBlob blob = item.getBlob(BynderItem.CLASSIFIER_ORIGINAL);
      if (blob != null) {
        contentModel.put(PROPERTY_DATA, blob);
      }
    } else {
      throw new IllegalArgumentException("transformation of type " + item.getClass().getName() + " not yet implemented");
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
    referenceModel.put(PROPERTY_DATA, new UrlBlobBuilder(owner, BynderItem.CLASSIFIER_PREVIEW).withUrl(imageUrl).build());
    referenceModel.put(PROPERTY_TITLE, "Video " + imageName);

    return referenceModel;
  }
}
