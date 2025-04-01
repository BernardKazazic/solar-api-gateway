package fer.solar.solar_api_gateway.auth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtScopeConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    @Override
    public Flux<GrantedAuthority> convert(@NonNull Jwt jwt) {
        return convertJwtToAuthorities(jwt);
    }

    private Flux<GrantedAuthority> convertJwtToAuthorities(@NonNull Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        authorities.addAll(extractScopesFromJwt(jwt));
        authorities.addAll(extractPermissionsFromJwt(jwt));
        
        return Flux.fromIterable(authorities);
    }

    List<GrantedAuthority> extractScopesFromJwt(Jwt jwt) {
        if (!jwt.getClaims().containsKey("scope")) {
            return new ArrayList<>();
        }

        String scopes = (String) jwt.getClaims().get("scope");
        if (scopes == null || scopes.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(scopes.split(" "))
                .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                .collect(Collectors.toList());
    }

    List<GrantedAuthority> extractPermissionsFromJwt(Jwt jwt) {
        if (!jwt.getClaims().containsKey("permissions")) {
            return new ArrayList<>();
        }

        Object permissionsObj = jwt.getClaims().get("permissions");
        if (!(permissionsObj instanceof Collection<?>)) {
            return new ArrayList<>();
        }

        Collection<?> permissionsColl = (Collection<?>) permissionsObj;
        return permissionsColl.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(permission -> new SimpleGrantedAuthority("PERMISSION_" + permission))
                .collect(Collectors.toList());
    }
} 