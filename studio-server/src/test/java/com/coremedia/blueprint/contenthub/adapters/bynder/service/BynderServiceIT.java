package com.coremedia.blueprint.contenthub.adapters.bynder.service;

import com.coremedia.labs.contenthub.adapters.bynder.service.BynderService;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Photo;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.PhotoSearchQuery;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.SearchResult;
import com.coremedia.labs.contenthub.adapters.bynder.service.model.Video;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BynderServiceIT {

  private BynderService testling;

  @Before
  public void setUp() {
    testling = new BynderService("https://coremedia-sandbox.bynder.com/",
            "7408c90ddc23f69504d46dd84f60791830d6befcee828797a4972f432ff53dd9");
  }

  @Test
  public void testSearchPhotos() {
    SearchResult<Photo> hits = testling.searchPhotos("look");
    assertNotNull(hits);
  }

  @Test
  public void testGetPhotoById() {
    int photoId = 4916080;
    Photo photo = testling.getPhotoById(photoId);
    assertNotNull(photo);
    assertEquals(photoId, photo.getId());
  }

  @Test
  public void testSearchVideos() {
    SearchResult<Video> hits = testling.searchVideos("fashion");
    assertNotNull(hits);
  }

  @Test
  public void testGetVideoById() {
    int videoId = 31377;
    Video video = testling.getVideoById(videoId);
    assertNotNull(video);
    assertEquals(videoId, video.getId());
  }

  @Test
  public void testSearchWithQuery() {
    PhotoSearchQuery query = PhotoSearchQuery.queryForTerm("beauty")
            .withEditorsChoice(false)
            .withSafeSearch(false)
            .withMinWidth(800)
            .withMinHeight(600);
    SearchResult<Photo> results = testling.searchPhotos(query);
    assertNotNull(results);
  }
}
