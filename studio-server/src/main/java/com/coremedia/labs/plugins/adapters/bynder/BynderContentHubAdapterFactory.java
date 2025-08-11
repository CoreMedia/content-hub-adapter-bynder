package com.coremedia.labs.plugins.adapters.bynder;

import com.coremedia.cap.struct.StructService;
import com.coremedia.contenthub.api.ContentHubAdapter;
import com.coremedia.contenthub.api.ContentHubAdapterFactory;
import com.coremedia.mimetype.MimeTypeService;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

@DefaultAnnotation(NonNull.class)
public class BynderContentHubAdapterFactory implements ContentHubAdapterFactory<BynderContentHubSettings> {

  private static final String ADAPTER_ID = "bynder";

  private final MimeTypeService mimeTypeService;
  private final StructService structService;

  public BynderContentHubAdapterFactory(@NonNull MimeTypeService mimeTypeService, @NonNull StructService structService) {
    this.mimeTypeService = mimeTypeService;
    this.structService = structService;
  }

  @Override
  public String getId() {
    return ADAPTER_ID;
  }

  @Override
  public ContentHubAdapter createAdapter(@NonNull BynderContentHubSettings settings,
                                         @NonNull String connectionId) {
    return new BynderContentHubAdapter(settings, connectionId, mimeTypeService, structService);
  }
}
