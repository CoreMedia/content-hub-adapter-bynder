package com.coremedia.blueprint.contenthub.adapters.bynder.service.model;

import com.coremedia.labs.contenthub.adapters.bynder.service.model.ImageSearchQuery;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageSearchQueryTest {

  @Test
  public void testQueryForTerm() {
    String searchTerm = "kitten";
    ImageSearchQuery kittenQuery = ImageSearchQuery.queryForTerm(searchTerm);
    assertEquals(searchTerm, kittenQuery.getTerm());
    assertEquals(-1, kittenQuery.getId());
  }

  @Test
  public void testQueryForId() {
    int searchId = 4916080;
    ImageSearchQuery kittenQuery = ImageSearchQuery.queryForId(searchId);
    assertNull(kittenQuery.getTerm());
    assertEquals(searchId, kittenQuery.getId());
  }

  @Test
  public void testAllFlags() {
    String searchTerm = "mode";
    ImageSearchQuery searchQuery = ImageSearchQuery.queryForTerm(searchTerm);
    assertEquals(searchTerm, searchQuery.getTerm());
    assertEquals(-1, searchQuery.getId());
  }
}
