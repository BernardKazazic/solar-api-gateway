package fer.solar.solar_api_gateway.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtScopeConverterTest {

    private JwtScopeConverter converter;
    private Jwt jwt;
    private Map<String, Object> claims;
    private Map<String, Object> headers;

    @BeforeEach
    void setUp() {
        converter = new JwtScopeConverter();
        claims = new HashMap<>();
        headers = new HashMap<>();
    }

    @Test
    void convert_should_extractScopes_when_jwtContainsScopes() {
        // Given
        claims.put("scope", "read:forecast write:forecast");
        jwt = createJwt(claims);

        // When
        Flux<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        StepVerifier.create(authorities.collectList())
            .assertNext(grantedAuthorities -> {
                assertThat(grantedAuthorities).hasSize(2);
                assertThat(grantedAuthorities)
                    .extracting("authority")
                    .contains("SCOPE_read:forecast", "SCOPE_write:forecast");
            })
            .verifyComplete();
    }

    @Test
    void convert_should_extractPermissions_when_jwtContainsPermissions() {
        // Given
        claims.put("permissions", Arrays.asList("read:data", "write:data"));
        jwt = createJwt(claims);

        // When
        Flux<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        StepVerifier.create(authorities.collectList())
            .assertNext(grantedAuthorities -> {
                assertThat(grantedAuthorities).hasSize(2);
                assertThat(grantedAuthorities)
                    .extracting("authority")
                    .contains("PERMISSION_read:data", "PERMISSION_write:data");
            })
            .verifyComplete();
    }

    @Test
    void convert_should_extractBothScopesAndPermissions_when_jwtContainsBoth() {
        // Given
        claims.put("scope", "read:forecast");
        claims.put("permissions", Collections.singletonList("admin:all"));
        jwt = createJwt(claims);

        // When
        Flux<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        StepVerifier.create(authorities.collectList())
            .assertNext(grantedAuthorities -> {
                assertThat(grantedAuthorities).hasSize(2);
                assertThat(grantedAuthorities)
                    .extracting("authority")
                    .contains("SCOPE_read:forecast", "PERMISSION_admin:all");
            })
            .verifyComplete();
    }

    @Test
    void convert_should_returnEmptyAuthorities_when_scopesAreEmpty() {
        // Given
        claims.put("scope", "");
        jwt = createJwt(claims);

        // When
        Flux<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        StepVerifier.create(authorities.collectList())
            .assertNext(grantedAuthorities -> 
                assertThat(grantedAuthorities).isEmpty())
            .verifyComplete();
    }

    @Test
    void convert_should_returnEmptyAuthorities_when_noScopesOrPermissionsExist() {
        // Given
        jwt = createJwt(claims);

        // When
        Flux<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        StepVerifier.create(authorities.collectList())
            .assertNext(grantedAuthorities -> 
                assertThat(grantedAuthorities).isEmpty())
            .verifyComplete();
    }

    private Jwt createJwt(Map<String, Object> claims) {
        return new Jwt(
            "token",
            Instant.now(),
            Instant.now().plusSeconds(300),
            headers,
            claims
        );
    }
} 