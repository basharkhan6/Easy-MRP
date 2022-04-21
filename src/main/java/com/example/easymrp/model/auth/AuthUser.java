package com.example.easymrp.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AuthUser implements UserDetails {

    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enable;

    private AuthUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enable) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enable = enable;
    }

    public User getUserModel() {
        return new User(id, username);
    }

    public static AuthUser build(User user) {
        // SimpleGrantedAuthority with ROLE_ prefix treated as role others are authority only
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            for (Privilege privilege : role.getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(privilege.getName()));
            }
        }
        return new AuthUser(user.getId(), user.getUsername(), user.getPassword(), authorities, user.isEnable());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuthUser user = (AuthUser) o;
        return Objects.equals(id, user.id);
    }

}
