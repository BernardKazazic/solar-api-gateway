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
            // Route for the forecast service
            .route("forecast-service", r -> r
                .path("/api/forecast/**")
                .filters(f -> f
                    .rewritePath("/api/forecast/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Gateway-Request", "true"))
                .uri("lb://forecast-service"))
                
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