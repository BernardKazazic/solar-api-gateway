package fer.solar.solar_api_gateway.routing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebFluxTest
@Import(RouteConfig.class)
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://test.auth0.com/",
    "auth0.audience=test-audience"
})
class RouteConfigTest {

    @Autowired
    private RouteConfig routeConfig;

    @Test
    @WithMockUser
    void customRouteLocator_should_configureRoutes_when_called() {
        // Given
        RouteLocatorBuilder builder = mock(RouteLocatorBuilder.class);
        RouteLocatorBuilder.Builder routesBuilder = mock(RouteLocatorBuilder.Builder.class);
        
        when(builder.routes()).thenReturn(routesBuilder);
        when(routesBuilder.route(anyString(), any())).thenReturn(routesBuilder);
        
        // When
        routeConfig.customRouteLocator(builder);

        // Then
        verify(builder).routes();
        verify(routesBuilder, atLeastOnce()).route(anyString(), any());
        verify(routesBuilder).build();
    }
} 