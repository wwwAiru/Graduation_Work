package com.golikov.bank.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER;

    //name() строковое представление роли
    @Override
    public String getAuthority() {
        return name();
    }
}
