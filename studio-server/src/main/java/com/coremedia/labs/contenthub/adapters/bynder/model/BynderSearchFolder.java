package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.labs.contenthub.adapters.bynder.service.model.SearchQuery;
import com.coremedia.contenthub.api.ContentHubObjectId;
import edu.umd.cs.findbugs.annotations.NonNull;

public class BynderSearchFolder extends BynderFolder {

  private SearchQuery query;

  public BynderSearchFolder(@NonNull ContentHubObjectId objectId, String name, SearchQuery query, BynderContentHubType type) {
    super(objectId, name, type);
    this.query = query;
  }

  public SearchQuery getQuery() {
    return query;
  }

  public void setQuery(SearchQuery query) {
    this.query = query;
  }

}
