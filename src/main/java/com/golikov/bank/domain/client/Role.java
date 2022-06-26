package com.golikov.bank.domain.client;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, HEAD_MANAGER;

    //name() строковое представление роли
    @Override
    public String getAuthority() {
        return name();
    }
}
