package fer.solar.solar_api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "service.uri")
public class ServiceUrisConfig {

    private String userManagement;
    private String mockFlask;

    // Getters and Setters

    public String getUserManagement() {
        return userManagement;
    }

    public void setUserManagement(String userManagement) {
        this.userManagement = userManagement;
    }

    public String getMockFlask() {
        return mockFlask;
    }

    public void setMockFlask(String mockFlask) {
        this.mockFlask = mockFlask;
    }
} 