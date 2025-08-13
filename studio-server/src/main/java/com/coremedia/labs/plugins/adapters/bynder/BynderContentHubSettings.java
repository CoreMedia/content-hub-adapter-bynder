package com.coremedia.labs.plugins.adapters.bynder;

public interface BynderContentHubSettings {

  String getApiEndpoint();
  String getAccessToken();
  String getDisplayName();
  Boolean getExternalReferenceModeEnabled();
  Boolean getThumbnailImportModeEnabled();
}
