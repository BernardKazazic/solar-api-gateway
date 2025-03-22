package fer.solar.solar_api_gateway.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
class ServiceRegistryConfigTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        DiscoveryClient discoveryClient() {
            return mock(DiscoveryClient.class);
        }
    }

    @Autowired
    private ServiceRegistryConfig serviceRegistryConfig;

    @Test
    void serviceRegistryConfig_should_beCreated_when_applicationContextLoads() {
        assertThat(serviceRegistryConfig).isNotNull();
    }

    @Test
    void enableDiscoveryClient_should_enableDiscoveryFeatures_when_applicationStarts() {
        // The @EnableDiscoveryClient annotation should enable discovery features
        // Here we're just verifying that the config is properly annotated
        assertThat(ServiceRegistryConfig.class.getAnnotations())
            .as("ServiceRegistryConfig should be annotated with @EnableDiscoveryClient")
            .anyMatch(annotation -> 
                annotation.annotationType().getSimpleName().equals("EnableDiscoveryClient"));
    }
} 