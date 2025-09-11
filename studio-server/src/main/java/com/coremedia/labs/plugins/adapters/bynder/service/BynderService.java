package com.coremedia.labs.plugins.adapters.bynder.service;

import com.coremedia.contenthub.api.ContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Collection;
import com.coremedia.labs.plugins.adapters.bynder.service.model.CollectionSearchResult;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Download;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Entity;
import com.coremedia.labs.plugins.adapters.bynder.service.model.MediaSearchQuery;
import com.coremedia.labs.plugins.adapters.bynder.service.model.MediaSearchResult;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Tag;
import com.google.common.base.Joiner;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class wrapping all API calls to Bynder API.
 * See <a href="https://bynder.docs.apiary.io/">Bynder API Documentation</a> for details.
 */
@Service
public class BynderService {

  private static final Logger LOG = LoggerFactory.getLogger(BynderService.class);

  // Query parameter names
  private static final String QUERY = "keyword";
  private static final String ID = "id";
  private static final String IDS = "ids";
  private static final String TOTAL = "total";
  private static final String LIMIT = "limit";
  private static final String PAGE = "page";
  private static final String TYPE = "type";
  private static final String ORDER_BY = "orderBy";
  private static final String COUNT = "count";
  private static final String KEYWORD = "keyword";
  private static final String TAGS = "tags";
  private static final String COLLECTION_ID = "collectionId";

  private static final int DEFAULT_PAGE = 1;
  public static final int DEFAULT_LIMIT = 50;
  public static final int MAX_LIMIT = 1000;
  private static final String DEFAULT_ORDER_BY = "dateModified desc";
  private static final int MIN_PER_PAGE = 3;
  public static final int MAX_PER_PAGE = 200;
  private static final int QUERY_PARAM_VALUE_ACTIVE = 1;

  private static final List<String> ASSET_TYPES = List.of("image" , "document" , "audio" , "video" , "3d");
  private static final String WILDCARD_SEARCH = "*";
  private static final String MEDIA_PATH = "media";
  private static final String MEDIA_ID_PATH = MEDIA_PATH + "/{id}";
  private static final String MEDIA_DOWNLOAD_PATH = MEDIA_ID_PATH + "/download";
  private static final String COLLECTIONS_PATH = "collections";
  private static final String COLLECTIONS_MEDIA_PATH = COLLECTIONS_PATH + "/{id}/media";
  private static final String TAGS_PATH = "tags";

  private static final String BEARER_HEADER_KEY = "Bearer ";

  private final RestTemplate restTemplate;
  private final String apiEndpoint;
  private final String accessToken;

  public BynderService(@NonNull String apiEndpoint, @NonNull String accessToken) {
    this.apiEndpoint = apiEndpoint;
    this.accessToken = accessToken;
    this.restTemplate = new RestTemplate();
  }

  // --- ASSETS --------------------------------------------------------------------------------------------------------

  /**
   * Retrieves a single asset.
   *
   * @param assetId The asset's ID. Required.
   * @return an {@link Optional} containing the asset
   * or an {@link Optional#empty()} no asset with the provided id could be fetched.
   */
  public Optional<Entity> getAssetById(@NonNull String assetId) {
    Map<String, Object> pathParams = Map.of(ID, assetId);
    ResponseEntity<Entity> response = performApiCall(MEDIA_ID_PATH, pathParams, null, new ParameterizedTypeReference<>() {
    });
    if (response != null && response.getStatusCode() == HttpStatus.OK) {
      return Optional.ofNullable(response.getBody());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Retrieve a list of assets for the given ids.
   * Note: This method still loads all pages for the given IDs as it's used internally.
   *
   * @param assetIds asset id
   * @return list of fetched assets
   */
  public List<Entity> getAssetsByIds(List<String> assetIds) {
    List<Entity> assets = new ArrayList<>();

    int page = 1;
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put(IDS, Joiner.on(",").join(assetIds));
    queryParams.put(PAGE, page);
    queryParams.put(ORDER_BY, "name asc");
    queryParams.put(LIMIT, 250);
    queryParams.put(COUNT, true);
    queryParams.put(TOTAL, true);

    boolean allFetched = false;
    while (!allFetched) {
      ResponseEntity<MediaSearchResult<Entity>> response = performApiCall(MEDIA_PATH, null, queryParams, new ParameterizedTypeReference<>() {
      });

      if (response != null) {
        MediaSearchResult<Entity> searchResult = response.getBody();
        if (searchResult != null) {
          assets.addAll(searchResult.getMedia());

          Optional<Integer> totalResultCount = Optional.ofNullable(searchResult.getCount()).map(MediaSearchResult.Count::getTotal);
          allFetched = totalResultCount.map(totalCount -> assets.size() >= totalCount).orElse(true);
        } else {
          allFetched = true;
        }

        if (!allFetched) {
          page++;
          queryParams.put(PAGE, page);
        }
      } else {
        allFetched = true;
      }
    }

    return assets;
  }

  /**
   * Retrieves the service-internal download URL of a single asset.
   *
   * @param assetId The asset's ID. Required.
   * @return an {@link Optional} with the asset's service-internal download URL.
   */
  public Optional<String> getAssetDownloadById(@NonNull String assetId) {
    Map<String, Object> pathParams = Map.of(ID, assetId);
    ResponseEntity<Download> response = performApiCall(MEDIA_DOWNLOAD_PATH, pathParams, null, new ParameterizedTypeReference<>() {
    });
    if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
      return Optional.ofNullable(response.getBody()).map(Download::getS3File);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Search assets with pagination support - SINGLE PAGE ONLY.
   *
   * @param query search query
   * @param page page number (1-based)
   * @param limit items per page
   * @return paginated search result
   */
  public @NonNull PaginatedResult<Entity> searchAssets(MediaSearchQuery query, int page, int limit) {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put(PAGE, Math.max(1, page));
    queryParams.put(ORDER_BY, DEFAULT_ORDER_BY);
    queryParams.put(LIMIT, Math.min(Math.max(MIN_PER_PAGE, limit), MAX_PER_PAGE));
    queryParams.put(COUNT, true);
    queryParams.put(TOTAL, true);

    // Limit result to specific type if present
    Optional.ofNullable(query.getType())
            .map(ContentHubType::getName)
            .map(String::toLowerCase)
            .ifPresent(type -> {
              if (ASSET_TYPES.contains(type)) {
                queryParams.put(TYPE, type);
              }
            });

    // Limit results to specific collection id if defined
    Optional.ofNullable(query.getCollectionId()).ifPresent(collectionId -> queryParams.put(COLLECTION_ID, collectionId));

    if (StringUtils.isNotBlank(query.getTerm())) {
      queryParams.put(KEYWORD, query.getTerm());
    }

    ResponseEntity<MediaSearchResult<Entity>> response = performApiCall(MEDIA_PATH, null, queryParams, new ParameterizedTypeReference<>() {
    });

    if (response != null && response.getBody() != null) {
      MediaSearchResult<Entity> searchResult = response.getBody();
      int totalCount = Optional.ofNullable(searchResult.getCount())
              .map(MediaSearchResult.Count::getTotal)
              .orElse(0);

      return new PaginatedResult<>(searchResult.getMedia(), page, limit, totalCount);
    }

    return new PaginatedResult<>(new ArrayList<>(), page, limit, 0);
  }

  // --- COLLECTIONS ---------------------------------------------------------------------------------------------------

  /**
   * Get collections with pagination support - SINGLE PAGE ONLY.
   *
   * @param page page number (1-based)
   * @param limit items per page
   * @return single page of collections
   */
  public PaginatedResult<Collection> getCollections(int page, int limit) {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put(PAGE, Math.max(1, page));
    queryParams.put(ORDER_BY, "name asc");
    queryParams.put(LIMIT, Math.min(Math.max(MIN_PER_PAGE, limit), MAX_PER_PAGE));
    queryParams.put(COUNT, true);
    queryParams.put(TOTAL, true);

    ResponseEntity<CollectionSearchResult> response = performApiCall(COLLECTIONS_PATH, null, queryParams, new ParameterizedTypeReference<>() {
    });

    if (response != null && response.getBody() != null) {
      CollectionSearchResult searchResult = response.getBody();
      int totalCount = searchResult.getCount();

      return new PaginatedResult<>(searchResult.getCollections(), page, limit, totalCount);
    }

    return new PaginatedResult<>(new ArrayList<>(), page, limit, 0);
  }

  /**
   * Retrieves the assets contained in a collection with pagination support.
   *
   * @param collectionId collection id
   * @param page page number (1-based)
   * @param limit items per page
   * @return paginated result
   */
  public PaginatedResult<Entity> getMediaInCollection(String collectionId, int page, int limit) {
    Map<String, Object> pathParams = Map.of(ID, collectionId);
    ResponseEntity<List<String>> listResponseEntity = performApiCall(COLLECTIONS_MEDIA_PATH, pathParams, null, new ParameterizedTypeReference<>() {
    });

    List<String> assetIds = listResponseEntity != null ? listResponseEntity.getBody() : null;
    if (assetIds == null || assetIds.isEmpty()) {
      return new PaginatedResult<>(new ArrayList<>(), page, limit, 0);
    }

    // Manual pagination for collection assets
    int totalCount = assetIds.size();
    int startIndex = (page - 1) * limit;
    int endIndex = Math.min(startIndex + limit, totalCount);

    if (startIndex >= totalCount) {
      return new PaginatedResult<>(new ArrayList<>(), page, limit, totalCount);
    }

    List<String> pageAssetIds = assetIds.subList(startIndex, endIndex);
    List<Entity> assets = getAssetsByIds(pageAssetIds);

    return new PaginatedResult<>(assets, page, limit, totalCount);
  }

  // --- TAGS ----------------------------------------------------------------------------------------------------------

  /**
   * Retrieves a list of tags.
   *
   * @return list of tags
   */
  public List<Tag> getTags() {
    ResponseEntity<List<Tag>> response = performApiCall(TAGS_PATH, null, null, new ParameterizedTypeReference<>() {
    });
    return Optional.ofNullable(response != null ? response.getBody() : null).orElse(Collections.emptyList());
  }

  /**
   * Get assets by tag with pagination support - SINGLE PAGE ONLY.
   *
   * @param tag tag name
   * @param page page number (1-based)
   * @param limit items per page
   * @return single page of assets with the tag
   */
  public PaginatedResult<Entity> getAssetsByTag(String tag, int page, int limit) {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put(TAGS, tag);
    queryParams.put(PAGE, Math.max(1, page));
    queryParams.put(ORDER_BY, "name asc");
    queryParams.put(LIMIT, Math.min(Math.max(MIN_PER_PAGE, limit), MAX_PER_PAGE));
    queryParams.put(COUNT, true);
    queryParams.put(TOTAL, true);

    ResponseEntity<MediaSearchResult<Entity>> response = performApiCall(MEDIA_PATH, null, queryParams, new ParameterizedTypeReference<>() {
    });

    if (response != null && response.getBody() != null) {
      MediaSearchResult<Entity> searchResult = response.getBody();
      int totalCount = Optional.ofNullable(searchResult.getCount())
              .map(MediaSearchResult.Count::getTotal)
              .orElse(0);

      return new PaginatedResult<>(searchResult.getMedia(), page, limit, totalCount);
    }

    return new PaginatedResult<>(new ArrayList<>(), page, limit, 0);
  }

  /**
   * Result class for paginated queries.
   */
  public static class PaginatedResult<T> {
    private final List<T> items;
    private final int page;
    private final int limit;
    private final int totalCount;

    public PaginatedResult(List<T> items, int page, int limit, int totalCount) {
      this.items = items;
      this.page = page;
      this.limit = limit;
      this.totalCount = totalCount;
    }

    public List<T> getItems() {
      return items;
    }

    public int getPage() {
      return page;
    }

    public int getLimit() {
      return limit;
    }

    public int getTotalCount() {
      return totalCount;
    }

    public boolean hasNextPage() {
      return (page * limit) < totalCount;
    }

    public int getTotalPages() {
      return (int) Math.ceil((double) totalCount / limit);
    }
  }

  // --- INTERNAL ------------------------------------------------------------------------------------------------------

  private <T> ResponseEntity<T> performApiCall(String path, Class<T> responseType) {
    return performApiCall(path, Map.of(), Map.of(), responseType);
  }

  private <T> ResponseEntity<T> performApiCall(String path, Map<String, Object> pathVariables, Map<String, Object> queryParams, Class<T> responseType) {
    return performApiCall(path, pathVariables, queryParams, ParameterizedTypeReference.forType(responseType));
  }

  private <T> ResponseEntity<T> performApiCall(String path, Map<String, Object> pathVariables, Map<String, Object> queryParams, ParameterizedTypeReference<T> responseType) {
    if (pathVariables == null) {
      pathVariables = Map.of();
    }

    if (queryParams == null) {
      queryParams = Map.of();
    }

    // Build headers
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, BEARER_HEADER_KEY + accessToken);

    // Build request entity
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    // Build request URI
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(buildUrl(path));

    // Add query params
    for (Map.Entry<String, Object> queryParam : queryParams.entrySet()) {
      String paramName = queryParam.getKey();
      Object paramValue = queryParam.getValue();
      if (paramName.equals(LIMIT)) {
        // Check "per_page" param value between min and max
        Integer p = (Integer) paramValue;
        if (p < 0 || p > MAX_PER_PAGE) {
          paramValue = MAX_PER_PAGE;
        } else if (p < MIN_PER_PAGE) {
          paramValue = MIN_PER_PAGE;
        }
      }

      uriBuilder.queryParam(paramName, paramValue);
    }

    // perform request
    String url = uriBuilder.buildAndExpand(pathVariables).toUriString();
    LOG.debug("GET - {}", url);
    ResponseEntity<String> debugResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    LOG.debug(debugResponse.getBody());
    try {
      return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    } catch (Exception e) {
      LOG.error("Unable to fetch resource: ", e);
    }
    return null;
  }

  private String buildUrl(String path) {
    return apiEndpoint + path;
  }

  private Map<String, Object> toQueryParamMap(@NonNull MediaSearchQuery mediaSearchQuery) {
    Map<String, Object> result = new HashMap<>();
    if (StringUtils.isNotBlank(mediaSearchQuery.getId())) {
      result.put(ID, mediaSearchQuery.getId());
    } else {
      String searchTerm = StringUtils.isBlank(mediaSearchQuery.getTerm())
              ? WILDCARD_SEARCH
              : mediaSearchQuery.getTerm().trim().replaceAll("\\s", "+");

      result.put(QUERY, searchTerm);
    }
    result.put(TOTAL, QUERY_PARAM_VALUE_ACTIVE);
    ContentHubType type = mediaSearchQuery.getType();
    if (type != null && !type.equals(BynderContentHubType.ALL.getType())) {
      result.put(TYPE, mediaSearchQuery.getType().getName());
    }
    return result;
  }
}
