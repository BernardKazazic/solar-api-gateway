package fer.solar.solar_api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "service.uri")
@Getter
@Setter
public class ServiceUrisConfig {

    private String userManagement;
    private String mockFlask;
    private String modelManagement;
    private String uiData;
    
} 