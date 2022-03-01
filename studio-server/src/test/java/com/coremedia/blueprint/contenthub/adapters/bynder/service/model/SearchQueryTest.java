package com.coremedia.blueprint.contenthub.adapters.bynder.service.model;

import com.coremedia.labs.plugins.adapters.bynder.model.BynderContentHubType;
import com.coremedia.labs.plugins.adapters.bynder.service.model.SearchQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SearchQueryTest {

  @Test
  public void testQueryForTerm() {
    String searchTerm = "kitten";
    SearchQuery kittenQuery = SearchQuery.queryForTerm(searchTerm);
    assertEquals(searchTerm, kittenQuery.getTerm());
    assertNull(kittenQuery.getId());
    assertNull(kittenQuery.getType());
  }

  @Test
  public void testQueryForId() {
    String searchId = "4916080";
    SearchQuery kittenQuery = SearchQuery.queryForId(searchId);
    assertNull(kittenQuery.getTerm());
    assertEquals(searchId, kittenQuery.getId());
    assertNull(kittenQuery.getType());
  }

  @Test
  public void testQueryForTermAndType() {
    String searchTerm = "mode";
    SearchQuery searchQuery = SearchQuery.queryForTerm(searchTerm).withType(BynderContentHubType.IMAGE.getType());
    assertEquals(searchTerm, searchQuery.getTerm());
    assertNull(searchQuery.getId());
    assertEquals(BynderContentHubType.IMAGE.getType(), searchQuery.getType());
  }
}
