package com.coremedia.labs.contenthub.adapters.bynder.service;

import com.coremedia.labs.contenthub.adapters.bynder.service.model.*;
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

  private static final String DEFAULT_API_ENDPOINT = "https://bynder.com/api/v4/";

  // Query parameter names
  private static final String QUERY = "keyword";
  private static final String ID = "id";
  private static final String TOTAL = "total";
  private static final String LIMIT = "limit";
  private static final String PAGE = "page";
  private static final String TYPE = "type";

  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_LIMIT = 20;
  private static final int MIN_PER_PAGE = 3;
  private static final int MAX_PER_PAGE = 200;
  private static final int QUERY_PARAM_VALUE_ACTIVE = 1;

  private static final String BEARER_HEADER_KEY = "Bearer ";

  private final RestTemplate restTemplate;
  private final String apiEndpoint;
  private final String apiKey;

  public BynderService(@NonNull String apiKey) {
    this(DEFAULT_API_ENDPOINT, apiKey);
  }

  public BynderService(@NonNull String apiEndpoint, @NonNull String apiKey) {
    this.apiEndpoint = apiEndpoint;
    this.apiKey = apiKey;
    this.restTemplate = new RestTemplate();
  }

  // --- COMMON (ALL ASSET TYPES)---------------------------------------------------------------------------------------

  /**
   * Retrieves a single asset.
   *
   * @param assetId The asset’s ID. Required.
   * @return the asset or <code>null</code>
   */
  public Entity getAssetById(@NonNull String assetId) {
    ResponseEntity<Entity> response = performApiCall("media",
            null,
            Map.of(ID, assetId),
            new ParameterizedTypeReference<>() {
            });
    if (response.getStatusCode().equals(HttpStatus.OK)) {
      return Optional.ofNullable(response.getBody())
              .orElse(null);
    } else {
      return null;
    }
  }

  // --- PHOTOS --------------------------------------------------------------------------------------------------------

  public SearchResult<Image> searchPhotos(@NonNull String query) {
    return searchPhotos(query, DEFAULT_PAGE, DEFAULT_LIMIT);
  }

  public SearchResult<Image> searchPhotos(@NonNull String query, int page, int limit) {
    if (StringUtils.isBlank(query)) {
      return new SearchResult<>();
    }

    // Replace all whitespace in query with '+'
    query = query.trim().replaceAll("\\s", "+");

    ResponseEntity<SearchResult<Image>> response = performApiCall("media",
            null,
            Map.of(
                    TOTAL, QUERY_PARAM_VALUE_ACTIVE,
                    QUERY, query,
                    LIMIT, limit,
                    PAGE, page,
                    TYPE, Image.TYPE
            ),
            new ParameterizedTypeReference<>() {
            });
    return response.getBody();
  }

  public SearchResult<Image> searchPhotos(@NonNull ImageSearchQuery query) {
    return searchPhotos(query, DEFAULT_PAGE, DEFAULT_LIMIT);
  }

  public SearchResult<Image> searchPhotos(@NonNull ImageSearchQuery query, int page, int limit) {
    if (StringUtils.isBlank(query.getTerm()) && query.getId() <= 0) {
      return new SearchResult<>();
    }

    Map<String, Object> queryParams = toQueryParamMap(query);
    queryParams.put(PAGE, page);
    queryParams.put(LIMIT, limit);

    ResponseEntity<SearchResult<Image>> response = performApiCall("media",
            null,
            queryParams,
            new ParameterizedTypeReference<>() {
            });

    return response.getBody();
  }


  // --- VIDEOS --------------------------------------------------------------------------------------------------------

  public SearchResult<Video> searchVideos(@NonNull String query) {
    return searchVideos(query, DEFAULT_PAGE, DEFAULT_LIMIT);
  }

  public SearchResult<Video> searchVideos(@NonNull String query, int page, int limit) {
    if (StringUtils.isBlank(query)) {
      return new SearchResult<>();
    }

    // Replace all whitespace in query with '+'
    query = query.trim().replaceAll("\\s", "+");

    ResponseEntity<SearchResult<Video>> response = performApiCall("media",
            null,
            Map.of(
                    TOTAL, QUERY_PARAM_VALUE_ACTIVE,
                    QUERY, query,
                    PAGE, page,
                    LIMIT, limit,
                    TYPE, Video.TYPE
            ),
            new ParameterizedTypeReference<>() {
            });
    return response.getBody();
  }

  public SearchResult<Video> searchVideos(@NonNull VideoSearchQuery query) {
    return searchVideos(query, DEFAULT_PAGE, DEFAULT_LIMIT);
  }

  public SearchResult<Video> searchVideos(@NonNull VideoSearchQuery query, int page, int limit) {
    if (StringUtils.isBlank(query.getTerm()) && query.getId() <= 0) {
      return new SearchResult<>();
    }

    Map<String, Object> queryParams = toQueryParamMap(query);
    queryParams.put(PAGE, page);
    queryParams.put(LIMIT, limit);

    ResponseEntity<SearchResult<Video>> response = performApiCall("media",
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
    headers.add(HttpHeaders.AUTHORIZATION, BEARER_HEADER_KEY + apiKey);

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
    if (searchQuery.getId() > 0) {
      result.put(ID, searchQuery.getId());
    } else {
      String searchTerm = searchQuery.getTerm().trim().replaceAll("\\s", "+");
      result.put(QUERY, searchTerm);
    }
    result.put(TOTAL, QUERY_PARAM_VALUE_ACTIVE);
    result.put(TYPE, searchQuery.getType());
    return result;
  }
}
