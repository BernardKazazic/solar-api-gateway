package fer.solar.solar_api_gateway.common;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class ServiceRegistryConfig {
    // This class enables service discovery for the application
    // Configuration is done through application.properties
} 