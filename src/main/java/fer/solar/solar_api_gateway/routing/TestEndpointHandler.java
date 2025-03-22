package fer.solar.solar_api_gateway.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class TestEndpointHandler {

    @Bean
    public RouterFunction<ServerResponse> testEndpoint() {
        return RouterFunctions.route(GET("/api/test"), request -> 
            ServerResponse.ok().bodyValue("Hello"));
    }
} 