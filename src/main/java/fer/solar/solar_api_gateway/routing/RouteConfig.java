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
                .route("user-management-service-users", r -> r
                        .path("/users/**")
                        .filters(f -> f
                        .rewritePath("/users(?<segment>/?.*)",
                         "/api/v1/users${segment}")
                        .addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getUserManagement()))
                .route("user-management-service-roles", r -> r
                        .path("/roles/**")
                        .filters(f -> f
                                .rewritePath("/roles(?<segment>/?.*)", "/api/v1/roles${segment}")
                                .addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getUserManagement()))
                .route("user-management-service-permissions", r -> r
                        .path("/permissions/**")
                        .filters(f -> f
                                .rewritePath("/permissions(?<segment>/?.*)", "/api/v1/permissions${segment}")
                                .addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getUserManagement()))

                .route("model-management-service-models", r -> r
                        .path("/models/**")
                        .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getModelManagement()))
                .route("model-management-service-model-features", r -> r
                        .path("/features/**")
                        .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getModelManagement()))

                .route("mock-upload", r -> r
                        .path("/upload/**")
                        .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getMockFlask()))
                .route("mock-power-plants", r -> r
                        .path("/power_plants/**")
                        .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getMockFlask()))
                // TODO remove when mock models is removed fully
                .route("mock-models-service-models", r -> r
                        .path("/models", "/models/")
                        .and()
                        .method("PUT", "DELETE", "PATCH", "OPTIONS", "HEAD", "TRACE")
                        .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                        .uri(serviceUrisConfig.getMockFlask()))
                .route("mock-models", r -> r
                        .path("/models/**")
                        .and()
                        .method("GET", "DELETE", "PATCH", "OPTIONS", "HEAD", "TRACE")
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