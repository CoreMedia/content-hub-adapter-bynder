package com.coremedia.labs.plugins.adapters.bynder;

import com.coremedia.cap.struct.StructService;
import com.coremedia.contenthub.api.*;
import com.coremedia.contenthub.api.column.ColumnProvider;
import com.coremedia.contenthub.api.exception.ContentHubException;
import com.coremedia.contenthub.api.pagination.PaginationRequest;
import com.coremedia.contenthub.api.search.ContentHubSearchResult;
import com.coremedia.contenthub.api.search.ContentHubSearchService;
import com.coremedia.contenthub.api.search.Sort;
import com.coremedia.contenthub.api.search.SortDirection;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderCollectionFolder;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderColumnProvider;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderFolder;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderItemFactory;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderTagFolder;
import com.coremedia.labs.plugins.adapters.bynder.service.BynderService;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Entity;
import com.coremedia.labs.plugins.adapters.bynder.service.model.MediaSearchQuery;
import com.coremedia.mimetype.MimeTypeService;
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
  private final BynderFolder assetsFolder;
  private final BynderFolder collectionsFolder;
  private final BynderFolder tagsFolder;
  private final BynderColumnProvider columnProvider;
  private final MimeTypeService mimeTypeService;
  private final StructService structService;
  private final BynderContentHubSettings bynderContentHubSettings;

  public BynderContentHubAdapter(BynderContentHubSettings settings, String connectionId, MimeTypeService mimeTypeService, StructService structService) {
    this.connectionId = connectionId;
    this.mimeTypeService = mimeTypeService;
    this.structService = structService;
    this.bynderContentHubSettings = settings;

    // base folders
    rootFolder = new BynderFolder(new ContentHubObjectId(connectionId, "root"), settings.getDisplayName(), BynderContentHubType.PORTAL_FOLDER);
    assetsFolder = new BynderFolder(new ContentHubObjectId(connectionId, "assets"), "Assets", BynderContentHubType.FOLDER);
    collectionsFolder = new BynderFolder(new ContentHubObjectId(connectionId, "collections"), "Collections", BynderContentHubType.FOLDER);
    tagsFolder = new BynderFolder(new ContentHubObjectId(connectionId, "tags"), "Tags", BynderContentHubType.FOLDER);

    rootFolder.addSubfolder(assetsFolder);
    rootFolder.addSubfolder(collectionsFolder);
    rootFolder.addSubfolder(tagsFolder);

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
    LOG.info("Get folder for id: {}", id);
    if (assetsFolder.getId().equals(id)) {
      return assetsFolder;
    } else if (collectionsFolder.getId().equals(id)) {
      return collectionsFolder;
    } else if (tagsFolder.getId().equals(id)) {
      return tagsFolder;
    } else {
      return getRootFolder(context);
    }
  }

  public List<Folder> getSubFolders(ContentHubContext context, Folder folder) throws ContentHubException {
    LOG.info("Get subfolders for folder: {}", folder.getId());
    List<Folder> subFolders = Collections.emptyList();

    // for root folder
    if (rootFolder == folder) {
      subFolders = rootFolder.getSubfolders();
    }

    // for collections folder
    if (collectionsFolder == folder) {
      subFolders = bynderService.getCollections().stream()
              .map(c -> BynderItemFactory.createCollectionFolder(new ContentHubObjectId(connectionId, c.getId()), c))
              .collect(Collectors.toList());
    }

    // for tags folder
    if (tagsFolder == folder) {
      subFolders = bynderService.getTags().stream()
              .map(tag -> BynderItemFactory.createTagFolder(new ContentHubObjectId(connectionId, tag.getId()), tag))
              .collect(Collectors.toList());
    }

    return subFolders;
  }

  @Nullable
  @Override
  public Folder getParent(ContentHubContext context, ContentHubObject contentHubObject) throws ContentHubException {
    LOG.info("Get parent for: {}", contentHubObject.getId());
    if (rootFolder == contentHubObject) {
      return null;
    }

    if (contentHubObject instanceof BynderCollectionFolder) {
      return collectionsFolder;
    }

    if (contentHubObject instanceof BynderTagFolder) {
      return tagsFolder;
    }

    return getRootFolder(context);
  }

  public List<Item> getItems(ContentHubContext context, Folder folder) throws ContentHubException {
    LOG.info("Get items for folder: {}", folder.getId());
    List<Item> items = Collections.emptyList();

    try {
      List<Entity> assets = Collections.emptyList();

      // Assets
      if (assetsFolder == folder) {
        LOG.info("Fetching media in assets folder.");
        assets = bynderService.searchAssets(MediaSearchQuery.queryForTerm("*"));
      }

      // Collection folders
      if (folder instanceof BynderCollectionFolder) {
        BynderCollectionFolder collectionFolder = (BynderCollectionFolder) folder;
        String collectionId = collectionFolder.getCollection().getId();
        LOG.info("Fetching media in collection '{}'.", collectionId);
        assets = bynderService.getMediaInCollection(collectionId);
      }

      // Tag folders
      if (folder instanceof BynderTagFolder) {
        BynderTagFolder tagFolder = (BynderTagFolder) folder;
        String tag = tagFolder.getTag().getTag();
        LOG.info("fetching media for tag '{}'.", tag);
        assets = bynderService.getAssetsByTag(tag);
      }

      items = assets.stream()
              .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m, bynderService, mimeTypeService))
              .collect(Collectors.toUnmodifiableList());

    } catch (Exception e) {
      LOG.warn("unable to get items for folder {}: ", folder, e);
    }

    return items;
  }

  @Nullable
  @Override
  public Item getItem(ContentHubContext context, ContentHubObjectId id) throws ContentHubException {
    LOG.info("Get item for id: {}", id);
    return bynderService.getAssetById(id.getExternalId())
            .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m, bynderService, mimeTypeService))
            .orElse(null);
  }

  @Override
  public GetChildrenResult getChildren(ContentHubContext context, Folder folder, @Nullable PaginationRequest paginationRequest) {
    LOG.info("Get children of folder: {}", folder.getId());
    List<ContentHubObject> children = new ArrayList<>();
    GetChildrenResult result = new GetChildrenResult(children);

    try {
      children.addAll(getSubFolders(context, folder));
      List<Item> items = getItems(context, folder);
      children.addAll(items);
      result = new GetChildrenResult(children);
    } catch (Exception e) {
      LOG.error("Unable to fetch children: ", e);
    }

    return result;
  }

  @Override
  public ContentHubTransformer transformer() {
    return new BynderContentHubTransformer(bynderContentHubSettings, structService);
  }


  // --- ContentHubSearchService ---------------------------------------------------------------------------------------

  private static final List<ContentHubType> SEARCH_TYPES = List.of(
          BynderContentHubType.ALL.getType(),
          BynderContentHubType.AUDIO.getType(),
          BynderContentHubType.DOCUMENT.getType(),
          BynderContentHubType.IMAGE.getType(),
          BynderContentHubType.THREE_D.getType(),
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
    LOG.info("Search for assets. {query={}, belowFolder={}, type={}}", query, belowFolder, type);

    if (rootFolder == belowFolder || collectionsFolder == belowFolder || tagsFolder == belowFolder) {
      return new ContentHubSearchResult(Collections.emptyList());
    }

    // search for id OR query term
    MediaSearchQuery mediaSearchQuery;
    if (query.startsWith(ID_PREFIX)) {
      String itemId = query.substring(ID_PREFIX.length());
      mediaSearchQuery = MediaSearchQuery.queryForId(itemId);
    } else {
      mediaSearchQuery = MediaSearchQuery.queryForTerm(query).withType(type).withLimit(limit);
    }

    // If the folder is a collection folder, limit search results to the collection
    Optional.ofNullable(belowFolder)
            .filter(BynderCollectionFolder.class::isInstance)
            .map(BynderCollectionFolder.class::cast)
            .map(BynderCollectionFolder::getCollection)
            .map(com.coremedia.labs.plugins.adapters.bynder.service.model.Collection::getId)
            .ifPresent(mediaSearchQuery::inCollection);

    List<Entity> assets = bynderService.searchAssets(mediaSearchQuery);

    ContentHubSearchResult result = new ContentHubSearchResult(assets.stream()
            .map(m -> BynderItemFactory.createItem(new ContentHubObjectId(connectionId, m.getId()), m, bynderService, mimeTypeService))
            .collect(Collectors.toUnmodifiableList()));

    LOG.info("Search returned {} hits.", result.getHits().size());
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
  public Set<Sort> supportedSortCriteria() {
    return Set.of(
            new Sort("name", SortDirection.ASCENDING),
            new Sort("name", SortDirection.DESCENDING),
            new Sort("dateCreated", SortDirection.ASCENDING),
            new Sort("dateCreated", SortDirection.DESCENDING),
            new Sort("dateModified", SortDirection.ASCENDING),
            new Sort("dateModified", SortDirection.DESCENDING),
            new Sort("datePublished", SortDirection.ASCENDING),
            new Sort("datePublished", SortDirection.DESCENDING)
    );
  }

  @Override
  public int supportedLimit() {
    return BynderService.MAX_LIMIT;
  }

  @Override
  public ColumnProvider columnProvider() {
    return columnProvider;
  }
}
