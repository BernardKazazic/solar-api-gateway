package fer.solar.solar_api_gateway.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtScopeConverterTest {

    private JwtScopeConverter jwtScopeConverter;

    @Mock
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwtScopeConverter = new JwtScopeConverter();
    }

    @Test
    void convert_shouldReturnEmptyFlux_whenJwtHasNoClaims() {
        // given
        Map<String, Object> claims = new HashMap<>();
        when(jwt.getClaims()).thenReturn(claims);

        // when
        Flux<GrantedAuthority> result = jwtScopeConverter.convert(jwt);

        // then
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void convert_shouldReturnScopesAndPermissions_whenJwtHasBoth() {
        // given
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "read write");
        claims.put("permissions", Arrays.asList("USER_READ", "USER_WRITE"));
        when(jwt.getClaims()).thenReturn(claims);

        // when
        Flux<GrantedAuthority> result = jwtScopeConverter.convert(jwt);

        // then
        StepVerifier.create(result)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void extractScopesFromJwt_shouldReturnEmptyList_whenScopeClaimIsMissing() {
        // given
        Map<String, Object> claims = new HashMap<>();
        when(jwt.getClaims()).thenReturn(claims);

        // when
        List<GrantedAuthority> result = jwtScopeConverter.extractScopesFromJwt(jwt);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void extractScopesFromJwt_shouldReturnEmptyList_whenScopeClaimIsEmpty() {
        // given
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "");
        when(jwt.getClaims()).thenReturn(claims);

        // when
        List<GrantedAuthority> result = jwtScopeConverter.extractScopesFromJwt(jwt);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void extractScopesFromJwt_shouldReturnScopes_whenScopeClaimHasValidScopes() {
        // given
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "read write");
        when(jwt.getClaims()).thenReturn(claims);

        // when
        List<GrantedAuthority> result = jwtScopeConverter.extractScopesFromJwt(jwt);

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("SCOPE_read", "SCOPE_write");
    }

    @Test
    void extractPermissionsFromJwt_shouldReturnEmptyList_whenPermissionsClaimIsMissing() {
        // given
        Map<String, Object> claims = new HashMap<>();
        when(jwt.getClaims()).thenReturn(claims);

        // when
        List<GrantedAuthority> result = jwtScopeConverter.extractPermissionsFromJwt(jwt);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void extractPermissionsFromJwt_shouldReturnEmptyList_whenPermissionsClaimIsNotCollection() {
        // given
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", "not a collection");
        when(jwt.getClaims()).thenReturn(claims);

        // when
        List<GrantedAuthority> result = jwtScopeConverter.extractPermissionsFromJwt(jwt);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void extractPermissionsFromJwt_shouldReturnPermissions_whenPermissionsClaimHasValidPermissions() {
        // given
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", Arrays.asList("USER_READ", "USER_WRITE"));
        when(jwt.getClaims()).thenReturn(claims);

        // when
        List<GrantedAuthority> result = jwtScopeConverter.extractPermissionsFromJwt(jwt);

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("PERMISSION_USER_READ", "PERMISSION_USER_WRITE");
    }

    @Test
    void extractPermissionsFromJwt_shouldFilterOutNonStringPermissions_whenPermissionsClaimHasMixedTypes() {
        // given
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", Arrays.asList("USER_READ", 123, "USER_WRITE", true));
        when(jwt.getClaims()).thenReturn(claims);

        // when
        List<GrantedAuthority> result = jwtScopeConverter.extractPermissionsFromJwt(jwt);

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("PERMISSION_USER_READ", "PERMISSION_USER_WRITE");
    }
} 