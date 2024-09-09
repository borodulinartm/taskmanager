package org.example.task_manager.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    // Константы
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    @Value("${jwt.client_id}")
    private String clientId;

    @Value("${jwt.username}")
    private String usernameClaim;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    private String getUsername(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (usernameClaim != null) {
            claimName = usernameClaim;
        }

        return jwt.getClaimAsString(claimName);
    }

    // Метод возвращает список scope (ролей на уровне пользователя)
    private Collection<? extends GrantedAuthority> getScopesFromJWT(@NonNull Jwt jwt) {
        if (jwt.getClaim(RESOURCE_ACCESS) == null) {
            return Set.of();
        }

        Map<String, Object> resource_access = jwt.getClaim(RESOURCE_ACCESS);
        if (resource_access.get(clientId) == null) {
            return Set.of();
        }

        // Extract roles
        Map<String, Object> resource = (Map<String, Object>) resource_access.get(clientId);
        List<String> roles = (List<String>) resource.get("roles");

        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("SCOPE_" + role.toUpperCase()))
                .toList();
    }

    private List<? extends GrantedAuthority> getRolesFromJWT(@NonNull Jwt jwt) {
        if (jwt.getClaim(REALM_ACCESS) == null) {
            return List.of();
        }

        Map<String, Object> roles = jwt.getClaim(REALM_ACCESS);
        if (roles.get(ROLES) == null) {
            return List.of();
        }

        List<String> rolesList = (List<String>) roles.get(ROLES);
        return rolesList
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .toList();
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        List<GrantedAuthority> authoritiesStream = new ArrayList<>(Stream
                .concat(jwtGrantedAuthoritiesConverter.convert(source).stream(),
                        getScopesFromJWT(source).stream()).toList());

        List<? extends GrantedAuthority> rolesStream = getRolesFromJWT(source);

        authoritiesStream.addAll(rolesStream);

        return new JwtAuthenticationToken(
                source,
                authoritiesStream,
                getUsername(source)
        );
    }
}
