package com.coremedia.labs.plugins.adapters.bynder.service;

import com.coremedia.labs.plugins.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Collection;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Entity;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Image;
import com.coremedia.labs.plugins.adapters.bynder.service.model.MediaSearchQuery;
import com.coremedia.labs.plugins.adapters.bynder.service.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    int limit = 3;
    MediaSearchQuery mediaSearchQuery = MediaSearchQuery.queryForTerm("summer").withType(BynderContentHubType.IMAGE.getType()).withLimit(limit);
    List<Entity> hits = testling.searchAssets(mediaSearchQuery);
    assertNotNull(hits);
    assertFalse(hits.isEmpty());
    assertEquals(limit, hits.size());
    assertTrue(hits.get(0) instanceof Image);
  }

  @Test
  public void testGetImageById() {
    String imageId = "CE6BAF80-5404-41BF-A7649A0339AAEE34";
    Optional<Entity> asset = testling.getAssetById(imageId);
    assertTrue(asset.isPresent());
    assertTrue(asset.get() instanceof Image);
    assertEquals(imageId, asset.get().getId());
  }

  @Test
  public void testSearchVideos() {
    MediaSearchQuery mediaSearchQuery = MediaSearchQuery.queryForTerm("summer").withType(BynderContentHubType.VIDEO.getType());
    List<Entity> hits = testling.searchAssets(mediaSearchQuery);
    assertNotNull(hits);
    assertFalse(hits.isEmpty());
    assertTrue(hits.get(0) instanceof Video);
  }

  @Test
  public void testGetVideoById() {
    String videoId = "1ACAEEA0-3B12-42D0-A9AB42377C220DDA";
    Optional<Entity> asset = testling.getAssetById(videoId);
    assertTrue(asset.isPresent());
    assertTrue(asset.get() instanceof Video);
    assertEquals(videoId, asset.get().getId());
  }

  @Test
  public void testGetCollections() {
    List<Collection> collections = testling.getCollections();
    assertEquals(2, collections.size());
  }

  @Test
  public void testGetAssetsByIds() {
    List<String> assetIds = List.of(
            "47C493CA-B7D4-428E-8176EB82145378CF",
            "B11145CF-CE88-4B48-B0F025CFE094BC8E",
            "BD7C1904-E669-4D99-A9A4508ADEB4EE79",
            "D5CDA6D2-9EA9-4C12-B3B510B884091465",
            "EABCFAC3-4E17-45C9-B71ED36E2FDC6344",
            "EFD64FBF-6F16-475A-B617283F03AB3170");
    List<Entity> assets = testling.getAssetsByIds(assetIds);
    assertEquals(6, assets.size());
  }

  @Test
  public void testGetAssetsByTags() {
    String tag = "Earrings";
    List<Entity> assets = testling.getAssetsByTags(List.of(tag));
    assertEquals(3, assets.size());
  }

}
