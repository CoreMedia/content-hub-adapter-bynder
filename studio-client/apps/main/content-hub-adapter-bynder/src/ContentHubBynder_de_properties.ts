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
dimensions_sectionItemKey: "Ma\u00DFe",
user_sectionItemKey: "Hinzugef\u00FCgt von",
size_sectionItemKey: "Dateigr\u00F6\u00DFe",
tags_sectionItemKey: "Schlagw\u00F6rter",
dateCreated_sectionItemKey: "Erstellungsdatum",
datePublished_sectionItemKey: "Publikationsdatum",
dateModified_sectionItemKey: "Letzte \u00C4nderung",
copyright_sectionItemKey: "Copyright",
description_sectionItemKey: "Beschreibung",

user_header : " Benutzer",
dateModified_header : " Letzte \u00C4nderung",
dateCreated_header : " Erstellungsdatum",
});
