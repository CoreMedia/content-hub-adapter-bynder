package com.coremedia.blueprint.contenthub.adapters.bynder.service;

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
  public void testSearchPhotos() {
    SearchResult<Image> hits = testling.searchPhotos("summer");
    assertNotNull(hits);
  }

  @Test
  public void testGetPhotoById() {
    String photoId = "CE6BAF80-5404-41BF-A7649A0339AAEE34";
    Entity asset = testling.getAssetById(photoId);
    assertTrue(asset instanceof Image);
    assertEquals(photoId, asset.getId());
  }

  @Test
  public void testSearchVideos() {
    SearchResult<Video> hits = testling.searchVideos("summer");
    assertNotNull(hits);
  }

  @Test
  public void testGetVideoById() {
    String videoId = "1ACAEEA0-3B12-42D0-A9AB42377C220DDA";
    Entity asset = testling.getAssetById(videoId);
    assertTrue(asset instanceof Video);
    assertEquals(videoId, asset.getId());
  }

  @Test
  public void testSearchWithQuery() {
    ImageSearchQuery query = ImageSearchQuery.queryForTerm("summer");
    SearchResult<Image> results = testling.searchPhotos(query);
    assertNotNull(results);
  }
}
