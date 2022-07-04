package com.golikov.bank.config.security;

import com.golikov.bank.domain.client.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Client client;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return client.getRoles();
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public String getUsername() {
        return client.getEmail();
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
    public boolean isEnabled() {
        return client.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
