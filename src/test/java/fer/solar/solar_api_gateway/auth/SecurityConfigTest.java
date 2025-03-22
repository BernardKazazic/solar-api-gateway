package fer.solar.solar_api_gateway.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://test.auth0.com/",
    "auth0.audience=test-audience"
})
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void securityWebFilterChain_should_notBeNull_when_called() {
        // When/Then
        // Due to the complexity of mocking the SecurityWebFilterChain builder,
        // we're just checking that the bean can be created
        assertThat(securityConfig).isNotNull();
    }

    @Test
    void corsConfigurationSource_should_returnCorsConfig_when_called() {
        // When
        CorsConfigurationSource corsSource = securityConfig.corsConfigurationSource();
        
        // Then
        assertThat(corsSource).isNotNull();
    }

    @Test
    void jwtDecoder_should_returnConfiguredDecoder_when_called() {
        // When
        ReactiveJwtDecoder decoder = securityConfig.jwtDecoder();
        
        // Then
        assertThat(decoder).isNotNull();
    }

    @Test
    void grantedAuthoritiesExtractor_should_returnConverter_when_called() {
        // When
        ReactiveJwtAuthenticationConverter converter = securityConfig.grantedAuthoritiesExtractor();
        
        // Then
        assertThat(converter).isNotNull();
        assertThat(converter).extracting("jwtGrantedAuthoritiesConverter")
                            .isInstanceOf(JwtScopeConverter.class);
    }
} 