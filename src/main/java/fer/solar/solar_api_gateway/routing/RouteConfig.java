package fer.solar.solar_api_gateway.routing;

import fer.solar.solar_api_gateway.config.ServiceUrisConfig;
import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final ServiceUrisConfig serviceUrisConfig;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Route for the user management service
            .route("user-management-service", r -> r
                .path("/users/**")
                .filters(f -> f
                    .rewritePath("/users(?<segment>/?.*)", "/api/v1/users${segment}")
                    .addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getUserManagement()))

            // Route for role management
            .route("role-management-service", r -> r
                .path("/roles/**")
                .filters(f -> f
                    .rewritePath("/roles(?<segment>/?.*)", "/api/v1/roles${segment}")
                    .addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getUserManagement()))

            // Route for permission management
            .route("permission-management-service", r -> r
                .path("/permissions/**")
                .filters(f -> f
                    .rewritePath("/permissions(?<segment>/?.*)", "/api/v1/permissions${segment}")
                    .addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getUserManagement()))
                
            // Routes for the mock Flask service (running on port 5000)
            .route("mock-upload", r -> r
                .path("/upload/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getMockFlask()))
            .route("mock-power-plants", r -> r
                .path("/power_plants/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getMockFlask()))
            .route("mock-models", r -> r
                .path("/models/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getMockFlask()))
             .route("mock-dashboard", r -> r
                .path("/dashboard/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getMockFlask()))
            .route("mock-events", r -> r
                .path("/events/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getMockFlask()))
            .route("mock-forecasts", r -> r
                .path("/forecasts/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getMockFlask()))
            .route("mock-metrics", r -> r
                .path("/metrics/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri(serviceUrisConfig.getMockFlask()))
                
            .build();
    }
} 