package com.coremedia.labs.contenthub.adapters.bynder;

import com.coremedia.contenthub.api.*;
import com.coremedia.contenthub.api.column.ColumnProvider;
import com.coremedia.contenthub.api.exception.ContentHubException;
import com.coremedia.contenthub.api.pagination.PaginationRequest;
import com.coremedia.contenthub.api.search.ContentHubSearchResult;
import com.coremedia.contenthub.api.search.ContentHubSearchService;
import com.coremedia.contenthub.api.search.Sort;
import com.coremedia.labs.contenthub.adapters.bynder.model.BynderColumnProvider;
import com.coremedia.labs.contenthub.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.contenthub.adapters.bynder.model.BynderFolder;
import com.coremedia.labs.contenthub.adapters.bynder.model.BynderItemFactory;
import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Entity;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.SearchQuery;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.SearchResult;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class BynderContentHubAdapter implements ContentHubAdapter, ContentHubSearchService {

  private static final Logger LOG = LoggerFactory.getLogger(BynderContentHubAdapter.class);
  private static final String ID_PREFIX = "id:";

  private final String connectionId;

  private final BynderService bynderService;

  private final BynderFolder rootFolder;

  private final BynderColumnProvider columnProvider;

  public BynderContentHubAdapter(BynderContentHubSettings settings, String connectionId) {
    this.connectionId = connectionId;

    rootFolder = new BynderFolder(new ContentHubObjectId(connectionId, "root"), settings.getDisplayName(), BynderContentHubType.FOLDER);
    bynderService = new BynderService(settings.getApiEndpoint(), settings.getAccessToken());
    columnProvider = new BynderColumnProvider();
  }

  // --- ContentHubAdapter ---------------------------------------------------------------------------------------------

  @Override
  public Folder getRootFolder(ContentHubContext context) throws ContentHubException {
    return rootFolder;
  }

  @Nullable
  @Override
  public Folder getFolder(ContentHubContext context, ContentHubObjectId id) throws ContentHubException {
    return getRootFolder(context);
  }

  public List<Folder> getSubFolders(ContentHubContext context, Folder folder) throws ContentHubException {
    if (folder instanceof BynderFolder) {
      BynderFolder parent = (BynderFolder) folder;
      return parent.getSubfolders();
    }
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public Folder getParent(ContentHubContext context, ContentHubObject contentHubObject) throws ContentHubException {
    return rootFolder == contentHubObject ? null : getRootFolder(context);
  }

  public List<Item> getItems(ContentHubContext context, Folder folder) throws ContentHubException {
    List<Item> items = Collections.emptyList();

    try {
      items = bynderService.search(SearchQuery.queryForTerm("*")).getMedia()
              .stream()
              .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
              .collect(Collectors.toUnmodifiableList());
    } catch (Exception e) {
      LOG.warn("Unable to get items for folder {}. {}", folder, e);
    }

    return items;
  }

  @Nullable
  @Override
  public Item getItem(ContentHubContext context, ContentHubObjectId id) throws ContentHubException {
    return Optional.ofNullable(bynderService.getAssetById(id.getExternalId()))
            .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
            .orElse(null);
  }

  @Override
  public GetChildrenResult getChildren(ContentHubContext context, Folder folder, @Nullable PaginationRequest paginationRequest) {
    List<ContentHubObject> children = new ArrayList<>();
    children.addAll(getSubFolders(context, folder));
    children.addAll(getItems(context, folder));
    return new GetChildrenResult(children);
  }

  @Override
  public ContentHubTransformer transformer() {
    return new BynderContentHubTransformer();
  }


  // --- ContentHubSearchService ---------------------------------------------------------------------------------------

  private static final List<ContentHubType> SEARCH_TYPES = List.of(
          BynderContentHubType.ALL.getType(),
          BynderContentHubType.IMAGE.getType(),
          BynderContentHubType.VIDEO.getType()
  );

  @NonNull
  @Override
  public Optional<ContentHubSearchService> searchService() {
    return Optional.of(this);
  }

  @Override
  public ContentHubSearchResult search(@NonNull String query,
                                       @Nullable Folder belowFolder,
                                       @Nullable ContentHubType type,
                                       Collection<String> filterQueries,
                                       List<Sort> sortCriteria,
                                       int limit) {

    ContentHubSearchResult result = new ContentHubSearchResult(Collections.emptyList());

    // search for id OR query term
    SearchQuery searchQuery;
    if (query.startsWith(ID_PREFIX)) {
      String itemId = query.substring(ID_PREFIX.length());
      searchQuery = SearchQuery.queryForId(itemId);
    } else {
      searchQuery = SearchQuery.queryForTerm(query).withType(type);
    }
    SearchResult<Entity> searchResult = bynderService.search(searchQuery);

    if (searchResult != null && searchResult.getMedia() != null) {
      result = new ContentHubSearchResult(
              searchResult.getMedia()
                      .stream()
                      .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
                      .collect(Collectors.toUnmodifiableList()));
    }
    return result;
  }

  @Override
  public boolean supportsSearchBelowFolder() {
    return true;
  }

  @Override
  public Collection<ContentHubType> supportedTypes() {
    return SEARCH_TYPES;
  }

  @Override
  public ColumnProvider columnProvider() {
    return columnProvider;
  }
}
