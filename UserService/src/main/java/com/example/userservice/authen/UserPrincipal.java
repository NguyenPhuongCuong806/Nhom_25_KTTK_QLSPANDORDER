package com.example.userservice.authen;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPrincipal implements UserDetails {
    Long id;
    String password;
    String username;
    String email;
    Collection authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {return true; }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
