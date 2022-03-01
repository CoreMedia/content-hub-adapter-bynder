import ContentHub_properties from "@coremedia/studio-client.main.content-hub-editor-components/ContentHub_properties";
import CopyResourceBundleProperties from "@coremedia/studio-client.main.editor-components/configuration/CopyResourceBundleProperties";
import StudioPlugin from "@coremedia/studio-client.main.editor-components/configuration/StudioPlugin";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import resourceManager from "@jangaroo/runtime/l10n/resourceManager";
import ContentHubBynder_properties from "./ContentHubBynder_properties";

interface ContentHubStudioBynderPluginConfig extends Config<StudioPlugin> {
}

class ContentHubStudioBynderPlugin extends StudioPlugin {
  declare Config: ContentHubStudioBynderPluginConfig;

  static readonly xtype: string = "com.coremedia.blueprint.studio.contenthub.bynder.ContentHubStudioBynderPlugin";

  constructor(config: Config<ContentHubStudioBynderPlugin> = null) {
    super(ConfigUtils.apply(Config(ContentHubStudioBynderPlugin, {

      configuration: [
        new CopyResourceBundleProperties({
          destination: resourceManager.getResourceBundle(null, ContentHub_properties),
          source: resourceManager.getResourceBundle(null, ContentHubBynder_properties),
        }),
      ],

    }), config));
  }
}

export default ContentHubStudioBynderPlugin;
