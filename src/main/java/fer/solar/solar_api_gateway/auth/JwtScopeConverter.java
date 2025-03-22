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
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Extract scopes
        if (jwt.getClaims().containsKey("scope")) {
            String scopes = (String) jwt.getClaims().get("scope");
            if (scopes != null && !scopes.isEmpty()) {
                authorities.addAll(
                    Arrays.stream(scopes.split(" "))
                        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                        .collect(Collectors.toList())
                );
            }
        }
        
        // Extract roles from permissions claim (common in Auth0)
        if (jwt.getClaims().containsKey("permissions")) {
            Object permissionsObj = jwt.getClaims().get("permissions");
            if (permissionsObj instanceof Collection<?>) {
                Collection<?> permissionsColl = (Collection<?>) permissionsObj;
                authorities.addAll(
                    permissionsColl.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(permission -> new SimpleGrantedAuthority("PERMISSION_" + permission))
                        .collect(Collectors.toList())
                );
            }
        }
        
        return Flux.fromIterable(authorities);
    }
} 