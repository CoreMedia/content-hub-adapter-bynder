import ResourceBundleUtil from "@jangaroo/runtime/l10n/ResourceBundleUtil";
import ContentHubBynder_properties from "./ContentHubBynder_properties";

/**
 * Overrides of ResourceBundle "ContentHubBynder" for Locale "de".
 * @see ContentHubBynder_properties#INSTANCE
 */
ResourceBundleUtil.override(ContentHubBynder_properties, {
  item_type_all_name: "Alle",
  item_type_image_name: "Bild",
  item_type_document_name: "Dokument",

  extension_sectionItemKey: "Dateityp",
  dimensions_sectionItemKey: "Maße",
  user_sectionItemKey: "Hinzugefügt von",
  size_sectionItemKey: "Dateigröße",
  tags_sectionItemKey: "Schlagwörter",
  dateCreated_sectionItemKey: "Erstellungsdatum",
  datePublished_sectionItemKey: "Publikationsdatum",
  dateModified_sectionItemKey: "Letzte Änderung",
  copyright_sectionItemKey: "Copyright",
  description_sectionItemKey: "Beschreibung",

  user_header: " Benutzer",
  dateModified_header: " Letzte Änderung",
  dateCreated_header: " Erstellungsdatum",
});
