package org.example.task_manager.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER(Set.of(Permission.USER_READ, Permission.USER_WRITE)),
    ADMIN(Set.of(Permission.ADMIN_READ, Permission.ADMIN_WRITE,
            Permission.USER_READ, Permission.USER_WRITE));

    private final Set<Permission> permissions;

    // Get the all authorities for the current user
    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .toList());
        // Why?
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
