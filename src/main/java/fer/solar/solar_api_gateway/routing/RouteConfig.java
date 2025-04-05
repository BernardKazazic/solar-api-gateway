package fer.solar.solar_api_gateway.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Route for the user management service
            .route("user-management-service", r -> r
                .path("/users/**")
                .filters(f -> f
                    .rewritePath("/users(?<segment>/?.*)", "/api/v1/users${segment}")
                    .addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:8081"))
                
            // Routes for the mock Flask service (running on port 5000)
            .route("mock-upload", r -> r
                .path("/upload/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:5000"))
            .route("mock-power-plants", r -> r
                .path("/power_plants/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:5000"))
            .route("mock-models", r -> r
                .path("/models/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:5000"))
             .route("mock-dashboard", r -> r
                .path("/dashboard/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:5000"))
            .route("mock-events", r -> r
                .path("/events/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:5000"))
            .route("mock-forecasts", r -> r
                .path("/forecasts/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:5000"))
            .route("mock-metrics", r -> r
                .path("/metrics/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Request", "true"))
                .uri("http://localhost:5000"))
                
            // Add more routes for other microservices here
            // Example:
            // .route("user-service", r -> r
            //     .path("/api/users/**")
            //     .filters(f -> f
            //         .rewritePath("/api/users/(?<segment>.*)", "/${segment}")
            //         .addRequestHeader("X-Gateway-Request", "true"))
            //     .uri("lb://user-service"))
                
            .build();
    }
} 