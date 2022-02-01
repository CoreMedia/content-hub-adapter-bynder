package com.coremedia.labs.contenthub.adapters.bynder.configuration;

import com.coremedia.labs.contenthub.adapters.bynder.BynderContentHubAdapterFactory;
import com.coremedia.labs.contenthub.adapters.bynder.BynderContentHubSettings;
import com.coremedia.contenthub.api.ContentHubAdapterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BynderContentHubAdapterConfiguration {

  @Bean
  public ContentHubAdapterFactory<BynderContentHubSettings> bynderContentHubAdapterFactory() {
    return new BynderContentHubAdapterFactory();
  }
}
