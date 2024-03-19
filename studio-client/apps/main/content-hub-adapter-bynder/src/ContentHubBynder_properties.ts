import CoreIcons_properties from "@coremedia/studio-client.core-icons/CoreIcons_properties";
import icon from "./icons/bynder_24.svg";
import SvgIconUtil from "@coremedia/studio-client.base-models/util/SvgIconUtil";

/**
 * Interface values for ResourceBundle "ContentHubBynder".
 * @see ContentHubBynder_properties#INSTANCE
 */
interface ContentHubBynder_properties {
  adapter_type_bynder_name: string;
  adapter_type_bynder_icon: string;

  folder_type_collection_name: string;
  folder_type_collection_icon: string;
  folder_type_tag_name: string;
  folder_type_tag_icon: string;

  item_type_all_name: string;
  item_type_image_name: string;
  item_type_video_name: string;
  item_type_document_name: string;
  item_type_audio_name: string;
  item_type_3d_name: string;
  id_sectionItemKey: string;
  extension_sectionItemKey: string;
  dimensions_sectionItemKey: string;
  user_sectionItemKey: string;
  size_sectionItemKey: string;
  tags_sectionItemKey: string;
  dateCreated_sectionItemKey: string;
  datePublished_sectionItemKey: string;
  dateModified_sectionItemKey: string;
  copyright_sectionItemKey: string;
  description_sectionItemKey: string;

  user_header: string;
  dateModified_header: string;
  dateCreated_header: string;
}

/**
 * Singleton for the current user Locale's instance of ResourceBundle "ContentHubBynder".
 * @see ContentHubBynder_properties
 */
const ContentHubBynder_properties: ContentHubBynder_properties = {
  adapter_type_bynder_name: "Bynder",
  adapter_type_bynder_icon: SvgIconUtil.getIconStyleClassForSvgIcon(icon),

  folder_type_collection_name: "Collection",
  folder_type_collection_icon: CoreIcons_properties.type_image_gallery,
  folder_type_tag_name: "Tag",
  folder_type_tag_icon: CoreIcons_properties.tag,

  item_type_all_name: "All",
  item_type_image_name: "Image",
  item_type_video_name: "Video",
  item_type_document_name: "Document",
  item_type_audio_name: "Audio",
  item_type_3d_name: "3D",

  id_sectionItemKey: "ID",
  extension_sectionItemKey: "File type",
  dimensions_sectionItemKey: "Dimensions",
  user_sectionItemKey: "Added by",
  size_sectionItemKey: "File size",
  tags_sectionItemKey: "Tags",
  dateCreated_sectionItemKey: "Date added",
  datePublished_sectionItemKey: "Date published",
  dateModified_sectionItemKey: "Last modified",
  copyright_sectionItemKey: "Copyright",
  description_sectionItemKey: "Description",

  user_header: "User",
  dateModified_header: "Last Modified",
  dateCreated_header: "Date Created",
};

export default ContentHubBynder_properties;
