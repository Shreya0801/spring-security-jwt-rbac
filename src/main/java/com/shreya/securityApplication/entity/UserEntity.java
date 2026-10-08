package com.shreya.securityApplication.entity;



import com.shreya.securityApplication.entity.enums.Role;
import com.shreya.securityApplication.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;

/*
    //for one role per use...
    private Role role;
*/

    // for multiple role per user we use Set. Whenever we want to fetch user we want to fetch all the role direclty so we have used FetchType.EAGER...
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
//    private Set<Permission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<SimpleGrantedAuthority> authorities = roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
//                .collect(Collectors.toSet());
          Set<SimpleGrantedAuthority> authorities = new HashSet<>();
          roles.forEach(
                  role-> {
                      Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
                      authorities.addAll(permissions);
                      authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
                  }

          );
//        permissions.forEach(
//                permission -> authorities.add(new SimpleGrantedAuthority(permission.name()))
//        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
