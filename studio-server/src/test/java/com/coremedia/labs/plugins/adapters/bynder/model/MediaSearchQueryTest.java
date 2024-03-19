package com.coremedia.labs.plugins.adapters.bynder.model;

import com.coremedia.labs.plugins.adapters.bynder.service.model.MediaSearchQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MediaSearchQueryTest {

  @Test
  public void testQueryForTerm() {
    String searchTerm = "kitten";
    MediaSearchQuery kittenQuery = MediaSearchQuery.queryForTerm(searchTerm);
    assertEquals(searchTerm, kittenQuery.getTerm());
    assertNull(kittenQuery.getId());
    assertNull(kittenQuery.getType());
  }

  @Test
  public void testQueryForId() {
    String searchId = "4916080";
    MediaSearchQuery kittenQuery = MediaSearchQuery.queryForId(searchId);
    assertNull(kittenQuery.getTerm());
    assertEquals(searchId, kittenQuery.getId());
    assertNull(kittenQuery.getType());
  }

  @Test
  public void testQueryForTermAndType() {
    String searchTerm = "mode";
    MediaSearchQuery mediaSearchQuery = MediaSearchQuery.queryForTerm(searchTerm).withType(BynderContentHubType.IMAGE.getType());
    assertEquals(searchTerm, mediaSearchQuery.getTerm());
    assertNull(mediaSearchQuery.getId());
    assertEquals(BynderContentHubType.IMAGE.getType(), mediaSearchQuery.getType());
  }
}
