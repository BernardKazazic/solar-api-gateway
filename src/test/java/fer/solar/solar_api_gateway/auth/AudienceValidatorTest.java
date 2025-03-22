package fer.solar.solar_api_gateway.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AudienceValidatorTest {

    private static final String EXPECTED_AUDIENCE = "solar-api";
    private AudienceValidator validator;
    private Map<String, Object> claims;
    private Map<String, Object> headers;

    @BeforeEach
    void setUp() {
        validator = new AudienceValidator(EXPECTED_AUDIENCE);
        claims = new HashMap<>();
        headers = new HashMap<>();
    }

    @Test
    void validate_should_returnSuccess_when_audienceIsCorrect() {
        // Given
        claims.put("aud", Arrays.asList(EXPECTED_AUDIENCE, "other-audience"));
        Jwt jwt = createJwt(claims);

        // When
        OAuth2TokenValidatorResult result = validator.validate(jwt);

        // Then
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    void validate_should_returnError_when_audienceIsMissing() {
        // Given
        claims.put("aud", Arrays.asList("wrong-audience"));
        Jwt jwt = createJwt(claims);

        // When
        OAuth2TokenValidatorResult result = validator.validate(jwt);

        // Then
        assertThat(result.hasErrors()).isTrue();
    }

    @Test
    void validate_should_returnError_when_audienceIsEmpty() {
        // Given
        claims.put("aud", Arrays.asList());
        Jwt jwt = createJwt(claims);

        // When
        OAuth2TokenValidatorResult result = validator.validate(jwt);

        // Then
        assertThat(result.hasErrors()).isTrue();
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