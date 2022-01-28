package com.coremedia.blueprint.contenthub.adapters.bynder.service.model;

import com.coremedia.labs.contenthub.adapters.bynder.service.model.PhotoSearchQuery;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoSearchQueryTest {

  @Test
  public void testQueryForTerm() {
    String searchTerm = "kitten";
    PhotoSearchQuery kittenQuery = PhotoSearchQuery.queryForTerm(searchTerm);
    assertEquals(searchTerm, kittenQuery.getTerm());
    assertEquals(-1, kittenQuery.getId());

    assertEquals(0, kittenQuery.getMinWidth());
    assertEquals(0, kittenQuery.getMinHeight());
    assertFalse(kittenQuery.isEditorsChoice());
    assertFalse(kittenQuery.isSafeSearch());
  }

  @Test
  public void testQueryForId() {
    int searchId = 4916080;
    PhotoSearchQuery kittenQuery = PhotoSearchQuery.queryForId(searchId);
    assertNull(kittenQuery.getTerm());
    assertEquals(searchId, kittenQuery.getId());

    assertEquals(0, kittenQuery.getMinWidth());
    assertEquals(0, kittenQuery.getMinHeight());
    assertFalse(kittenQuery.isEditorsChoice());
    assertFalse(kittenQuery.isSafeSearch());
  }

  @Test
  public void testAllFlags() {
    String searchTerm = "mode";
    PhotoSearchQuery searchQuery = PhotoSearchQuery.queryForTerm(searchTerm)
            .withMinWidth(800)
            .withMinHeight(600)
            .withEditorsChoice(false)
            .withSafeSearch(true);
    assertEquals(searchTerm, searchQuery.getTerm());
    assertEquals(-1, searchQuery.getId());
    assertEquals(800, searchQuery.getMinWidth());
    assertEquals(600, searchQuery.getMinHeight());
    assertFalse(searchQuery.isEditorsChoice());
    assertTrue(searchQuery.isSafeSearch());
  }
}
