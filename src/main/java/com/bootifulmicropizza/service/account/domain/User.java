package com.bootifulmicropizza.service.account.domain;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * {@link org.springframework.security.core.userdetails.User} subclass representing the logged in user.
 */
public class User extends org.springframework.security.core.userdetails.User {

    private String accountId;

    public User(String accountId, String username, String password, boolean enabled,
                boolean accountNonExpired, boolean credentialsNonExpired,
                boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
