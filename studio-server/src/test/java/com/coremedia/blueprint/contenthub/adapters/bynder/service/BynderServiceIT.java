package com.coremedia.blueprint.contenthub.adapters.bynder.service;

import com.coremedia.labs.contenthub.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BynderServiceIT {

  private BynderService testling;

  @Before
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
