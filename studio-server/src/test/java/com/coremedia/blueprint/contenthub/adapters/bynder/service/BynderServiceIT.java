package com.coremedia.blueprint.contenthub.adapters.bynder.service;

import com.coremedia.labs.plugins.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.service.BynderService;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Entity;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Image;
import com.coremedia.labs.plugins.adapters.bynder.service.model.SearchQuery;
import com.coremedia.labs.plugins.adapters.bynder.service.model.SearchResult;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BynderServiceIT {

  private BynderService testling;

  @BeforeEach
  public void setUp() {
    testling = new BynderService("https://coremedia-sandbox.bynder.com/api/v4/",
            "7408c90ddc23f69504d46dd84f60791830d6befcee828797a4972f432ff53dd9");
  }

  @Test
  public void testSearchImages() {
    SearchQuery searchQuery = SearchQuery.queryForTerm("summer").withType(BynderContentHubType.IMAGE.getType());
    SearchResult<Entity> hits = testling.search(searchQuery);
    assertNotNull(hits);
    assertTrue(hits.getMedia().size() != 0);
    assertTrue(hits.getMedia().get(0) instanceof Image);
  }

  @Test
  public void testGetImageById() {
    String imageId = "CE6BAF80-5404-41BF-A7649A0339AAEE34";
    Entity asset = testling.getAssetById(imageId);
    assertTrue(asset instanceof Image);
    assertEquals(imageId, asset.getId());
  }

  @Test
  public void testSearchVideos() {
    SearchQuery searchQuery = SearchQuery.queryForTerm("summer").withType(BynderContentHubType.VIDEO.getType());
    SearchResult<Entity> hits = testling.search(searchQuery);
    assertNotNull(hits);
    assertTrue(hits.getMedia().size() != 0);
    assertTrue(hits.getMedia().get(0) instanceof Video);
  }

  @Test
  public void testGetVideoById() {
    String videoId = "1ACAEEA0-3B12-42D0-A9AB42377C220DDA";
    Entity asset = testling.getAssetById(videoId);
    assertTrue(asset instanceof Video);
    assertEquals(videoId, asset.getId());
  }
}
