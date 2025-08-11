package com.coremedia.labs.plugins.adapters.bynder;

import com.coremedia.cap.struct.Struct;
import com.coremedia.cap.struct.StructBuilder;
import com.coremedia.cap.struct.StructService;
import com.coremedia.contenthub.api.*;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderImageItem;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderItem;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderVideoItem;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.Boolean;
import java.util.List;

public class BynderContentHubTransformer implements ContentHubTransformer {

  private static final Logger LOG = LoggerFactory.getLogger(BynderContentHubTransformer.class);

  private static final String PROPERTY_TITLE = "title";
  private static final String PROPERTY_DATA = "data";
  private static final String PROPERTY_DATA_URL = "dataUrl";
  private static final String PROPERTY_COPYRIGHT = "copyright";
  private static final String PROPERTY_DETAIL_TEXT = "detailText";
  private static final String PROPERTY_LOCAL_SETTINGS = "localSettings";

  private static final String PROPERTY_ASSET_INFO_STRUCT = "assetInfo";
  private static final String PROPERTY_ID = "id";
  private static final String PROPERTY_WIDTH = "width";
  private static final String PROPERTY_HEIGHT = "height";
  private static final String PROPERTY_EXTENSION = "extension";
  private static final String PROPERTY_IS_THUMBNAIL_MODE = "isThumbnailMode";

  private final BynderContentHubSettings settings;
  private final StructService structService;

  public BynderContentHubTransformer(final BynderContentHubSettings settings, final StructService structService) {
    this.structService = structService;
    this.settings = settings;
  }

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

    if (item instanceof BynderImageItem || item instanceof BynderVideoItem) {

      if(item instanceof BynderImageItem && settings.getThumbnailImportModeEnabled()) {
        contentModel.put(PROPERTY_DATA, item.getThumbnailBlob());
      }
      else {
        ContentHubBlob blob = item.getBlob(BynderItem.CLASSIFIER_ORIGINAL);
        if (blob != null) {
          contentModel.put(PROPERTY_DATA, blob);
        }
      }

      if (settings.getExternalReferenceModeEnabled()) {

        StructBuilder assetInfoStructBuilder = structService.createStructBuilder();
        assetInfoStructBuilder.declareString(PROPERTY_ID, 1024, item.getId().getExternalId());
        List<String> extensions = item.getExtension();
        if(extensions != null) {
          assetInfoStructBuilder.declareStrings(PROPERTY_EXTENSION, 1024, extensions);
        }

        if (item instanceof BynderImageItem) {
          // For images, store the transform base url property, which we can append parameters to later
          BynderImageItem bynderImageItem = (BynderImageItem) item;
          contentModel.put(PROPERTY_DATA_URL, bynderImageItem.getTransformBaseUrl());
          assetInfoStructBuilder.declareInteger(PROPERTY_WIDTH, bynderImageItem.getImageBaseWidth());
          assetInfoStructBuilder.declareInteger(PROPERTY_HEIGHT, bynderImageItem.getImageBaseHeight());
          if(settings.getThumbnailImportModeEnabled()) {
            assetInfoStructBuilder.declareBoolean(PROPERTY_IS_THUMBNAIL_MODE, Boolean.valueOf(true));
          }
        } else {
          // For videos, the data URL is what we want
          contentModel.put(PROPERTY_DATA_URL, item.getDataUrl());
        }

        StructBuilder localSettingsStructBuilder = structService.createStructBuilder();
        localSettingsStructBuilder.declareStruct(PROPERTY_ASSET_INFO_STRUCT, assetInfoStructBuilder.build());
        contentModel.put(PROPERTY_LOCAL_SETTINGS, localSettingsStructBuilder.build());
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
