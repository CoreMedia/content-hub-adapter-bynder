package com.coremedia.labs.plugins.adapters.bynder.service;

import com.coremedia.contenthub.api.ContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Download;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Entity;
import com.coremedia.labs.plugins.adapters.bynder.service.model.SearchQuery;
import com.coremedia.labs.plugins.adapters.bynder.service.model.SearchResult;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
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
  private static final String TOTAL = "total";
  private static final String LIMIT = "limit";
  private static final String PAGE = "page";
  private static final String TYPE = "type";
  private static final String ORDER_BY = "orderBy";

  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_LIMIT = 20;
  private static final String DEFAULT_ORDER_BY = "dateModified desc";
  private static final int MIN_PER_PAGE = 3;
  private static final int MAX_PER_PAGE = 200;
  private static final int QUERY_PARAM_VALUE_ACTIVE = 1;
  private static final String WILDCARD_SEARCH = "*";
  private static final String MEDIA_ID_ENDPOINT_PATH = "media/{id}";
  private static final String MEDIA_DOWNLOAD_ENDPOINT_PATH = "media/{id}/download";
  private static final String MEDIA_SEARCH_ENDPOINT_PATH = "media";

  private static final String BEARER_HEADER_KEY = "Bearer ";

  private final RestTemplate restTemplate;
  private final String apiEndpoint;
  private final String accessToken;

  public BynderService(@NonNull String apiEndpoint, @NonNull String accessToken) {
    this.apiEndpoint = apiEndpoint;
    this.accessToken = accessToken;
    this.restTemplate = new RestTemplate();
  }

  /**
   * Retrieves a single asset.
   *
   * @param assetId The asset’s ID. Required.
   * @return the asset or <code>null</code>
   */
  public Entity getAssetById(@NonNull String assetId) {
    ResponseEntity<Entity> response = performApiCall(MEDIA_ID_ENDPOINT_PATH,
            Map.of(ID, assetId),
            null,
            new ParameterizedTypeReference<>() {
            });
    if (response.getStatusCode().equals(HttpStatus.OK)) {
      return Optional.ofNullable(response.getBody())
              .orElse(null);
    } else {
      return null;
    }
  }

  /**
   * Retrieves the service-internal download URL of a single asset.
   *
   * @param assetId The asset’s ID. Required.
   * @return the asset's service-internal download URL.
   */
  public String getAssetDownloadById(@NonNull String assetId) {
    ResponseEntity<Download> response = performApiCall(MEDIA_DOWNLOAD_ENDPOINT_PATH,
            Map.of(ID, assetId),
            null,
            new ParameterizedTypeReference<>() {
            });
    if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
      return response.getBody().getS3File();
    } else {
      return null;
    }
  }

  public SearchResult<Entity> search(SearchQuery query) {
    return search(query, DEFAULT_PAGE, DEFAULT_LIMIT);
  }

  public SearchResult<Entity> search(SearchQuery query, int page, int limit) {
    Map<String, Object> queryParams = toQueryParamMap(query);
    queryParams.put(PAGE, page);
    queryParams.put(LIMIT, limit);
    queryParams.put(ORDER_BY, DEFAULT_ORDER_BY);

    ResponseEntity<SearchResult<Entity>> response = performApiCall(MEDIA_SEARCH_ENDPOINT_PATH,
            null,
            queryParams,
            new ParameterizedTypeReference<>() {
            });

    return response.getBody();
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
    //ResponseEntity<String> debugResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
  }

  private String buildUrl(String path) {
    return apiEndpoint + path;
  }

  private Map<String, Object> toQueryParamMap(@NonNull SearchQuery searchQuery) {
    Map<String, Object> result = new HashMap<>();
    if (StringUtils.isNotBlank(searchQuery.getId())) {
      result.put(ID, searchQuery.getId());
    } else {
      String searchTerm = StringUtils.isBlank(searchQuery.getTerm())
              ? WILDCARD_SEARCH
              : searchQuery.getTerm().trim().replaceAll("\\s", "+");

      result.put(QUERY, searchTerm);
    }
    result.put(TOTAL, QUERY_PARAM_VALUE_ACTIVE);
    ContentHubType type = searchQuery.getType();
    if (type != null && !type.equals(BynderContentHubType.ALL.getType())) {
      result.put(TYPE, searchQuery.getType().getName());
    }
    return result;
  }
}
