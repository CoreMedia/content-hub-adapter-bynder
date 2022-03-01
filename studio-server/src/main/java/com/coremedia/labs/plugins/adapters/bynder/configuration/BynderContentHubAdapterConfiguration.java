package com.coremedia.labs.plugins.adapters.bynder.configuration;

import com.coremedia.labs.plugins.adapters.bynder.BynderContentHubAdapterFactory;
import com.coremedia.labs.plugins.adapters.bynder.BynderContentHubSettings;
import com.coremedia.contenthub.api.ContentHubAdapterFactory;
import com.coremedia.mimetype.MimeTypeService;
import com.coremedia.mimetype.MimeTypeServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MimeTypeServiceConfiguration.class)
public class BynderContentHubAdapterConfiguration {

  @Bean
  public ContentHubAdapterFactory<BynderContentHubSettings> bynderContentHubAdapterFactory(MimeTypeService mimeTypeService) {
    return new BynderContentHubAdapterFactory(mimeTypeService);
  }
}
