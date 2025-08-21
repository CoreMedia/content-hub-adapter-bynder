package com.coremedia.labs.plugins.adapters.bynder.configuration;

import com.coremedia.cap.common.CapConnection;
import com.coremedia.labs.plugins.adapters.bynder.BynderContentHubAdapterFactory;
import com.coremedia.labs.plugins.adapters.bynder.BynderContentHubSettings;
import com.coremedia.contenthub.api.ContentHubAdapterFactory;
import com.coremedia.mimetype.MimeTypeService;
import com.coremedia.mimetype.MimeTypeServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.coremedia.cms.common.plugins.beans_for_plugins.CommonBeansForPluginsConfiguration;

@Configuration
@Import({MimeTypeServiceConfiguration.class, CommonBeansForPluginsConfiguration.class})
public class BynderContentHubAdapterConfiguration {

  @Bean
  public ContentHubAdapterFactory<BynderContentHubSettings> bynderContentHubAdapterFactory(MimeTypeService mimeTypeService, CapConnection connection) {
    return new BynderContentHubAdapterFactory(mimeTypeService, connection);
  }
}
