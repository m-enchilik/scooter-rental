package ru.senla.javacourse.enchilik.scooterrental.core.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER"),
    ;

    private final String vale;

    Role(String vale) {
        this.vale = vale;
    }

    @Override
    public String getAuthority() {
        return vale;
    }

}
