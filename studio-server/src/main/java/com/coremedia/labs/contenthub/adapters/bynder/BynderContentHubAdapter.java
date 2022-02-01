package com.coremedia.labs.contenthub.adapters.bynder;

import com.coremedia.contenthub.api.*;
import com.coremedia.contenthub.api.exception.ContentHubException;
import com.coremedia.contenthub.api.pagination.PaginationRequest;
import com.coremedia.contenthub.api.search.ContentHubSearchResult;
import com.coremedia.contenthub.api.search.ContentHubSearchService;
import com.coremedia.contenthub.api.search.Sort;
import com.coremedia.labs.contenthub.adapters.bynder.model.*;
import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.*;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class BynderContentHubAdapter implements ContentHubAdapter, ContentHubSearchService {

  private static final Logger LOG = LoggerFactory.getLogger(BynderContentHubAdapter.class);

  private final String connectionId;
  private final BynderContentHubSettings settings;

  private final BynderService bynderService;

  private final BynderFolder rootFolder;

  public BynderContentHubAdapter(BynderContentHubSettings settings, String connectionId) {
    this.settings = settings;
    this.connectionId = connectionId;

    rootFolder = new BynderFolder(new ContentHubObjectId(connectionId, "root"), settings.getDisplayName(), BynderContentHubType.FOLDER);
    BynderFolder imagesRootFolder = new BynderSearchFolder(new ContentHubObjectId(connectionId, "images"), "Images", ImageSearchQuery.queryForTerm("*"), BynderContentHubType.IMAGE);
    BynderFolder videosRootFolder = new BynderSearchFolder(new ContentHubObjectId(connectionId, "video"), "Videos", VideoSearchQuery.queryForTerm("*"), BynderContentHubType.VIDEO);

    rootFolder.addSubfolder(imagesRootFolder);
    rootFolder.addSubfolder(videosRootFolder);

    bynderService = new BynderService(settings.getApiEndpoint(), settings.getAccessToken());
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
      if (rootFolder == folder) {
        items = bynderService.searchPhotos(ImageSearchQuery.queryForTerm("*")).getMedia()
                .stream()
                .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
                .collect(Collectors.toUnmodifiableList());
      } else if (folder instanceof BynderSearchFolder) {
        BynderSearchFolder searchFolder = (BynderSearchFolder) folder;
        SearchQuery query = searchFolder.getQuery();

        if (query instanceof ImageSearchQuery) {
          items = bynderService.searchPhotos((ImageSearchQuery) query).getMedia()
                  .stream()
                  .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
                  .collect(Collectors.toUnmodifiableList());
        } else if (query instanceof VideoSearchQuery) {
          bynderService.searchVideos((VideoSearchQuery) query);
          items = bynderService.searchVideos((VideoSearchQuery) query).getMedia()
                  .stream()
                  .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
                  .collect(Collectors.toUnmodifiableList());
        }
      }
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

    if (StringUtils.isBlank(query)) {
      return result;
    }

    // Search for id?
    int entityId = 0;
    if (query.startsWith("id:")) {
      String itemId = query.replace("id:", "");
      try {
        entityId = Integer.parseInt(itemId);
      } catch (NumberFormatException e) {
        // ignore
      }
    }

    // Image Search
    if (BynderContentHubType.IMAGE.getType().equals(type)) {
      SearchResult<Image> searchResult;

      if (entityId > 0) {
        searchResult = bynderService.searchPhotos(ImageSearchQuery.queryForId(entityId));

      } else if (belowFolder instanceof BynderSearchFolder) {
        BynderSearchFolder belowSearchFolder = (BynderSearchFolder) belowFolder;
        ImageSearchQuery sq = ImageSearchQuery.fromQuery((ImageSearchQuery) belowSearchFolder.getQuery());
        sq.withTerm(query);
        searchResult = bynderService.searchPhotos(sq);

      } else {
        searchResult = bynderService.searchPhotos(query, 1, limit);
      }

      if (searchResult != null && searchResult.getMedia() != null) {
        result = new ContentHubSearchResult(
                searchResult.getMedia()
                        .stream()
                        .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
                        .collect(Collectors.toUnmodifiableList()));
      }

      // Video Search
    } else if (BynderContentHubType.VIDEO.getType().equals(type)) {
      SearchResult<Video> searchResult;

      if (entityId > 0) {
        searchResult = bynderService.searchVideos(VideoSearchQuery.queryForId(entityId));

      } else if (belowFolder instanceof BynderSearchFolder) {
        BynderSearchFolder belowSearchFolder = (BynderSearchFolder) belowFolder;
        VideoSearchQuery sq = VideoSearchQuery.fromQuery((VideoSearchQuery) belowSearchFolder.getQuery());
        sq.withTerm(query);
        searchResult = bynderService.searchVideos(sq);

      } else {
        searchResult = bynderService.searchVideos(query, 1, limit);
      }

      if (searchResult != null && searchResult.getMedia() != null) {
        result = new ContentHubSearchResult(
                searchResult.getMedia()
                        .stream()
                        .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m))
                        .collect(Collectors.toUnmodifiableList()));
      }
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
}
