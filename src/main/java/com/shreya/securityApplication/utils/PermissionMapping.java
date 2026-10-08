package com.shreya.securityApplication.utils;

import com.shreya.securityApplication.entity.enums.Permission;
import com.shreya.securityApplication.entity.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.shreya.securityApplication.entity.enums.Permission.*;
import static com.shreya.securityApplication.entity.enums.Role.*;

public class PermissionMapping {

    private static final Map<Role,Set<Permission>> map = Map.of(USER, Set.of(USER_VIEW, POST_VIEW),
            CREATOR, Set.of(POST_CREATE, POST_UPDATE, USER_UPDATE),
            ADMIN, Set.of(POST_CREATE, POST_UPDATE, POST_DELETE, USER_CREATE, USER_UPDATE, USER_DELETE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}
