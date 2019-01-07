package com.bootifulmicropizza.service.account.domain;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * {@link org.springframework.security.core.userdetails.User} subclass representing the logged in user.
 */
public class UserDetails extends org.springframework.security.core.userdetails.User {

    private String customerNumber;

    public UserDetails(String customerNumber, String username, String password, boolean enabled,
                       boolean accountNonExpired, boolean credentialsNonExpired,
                       boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.customerNumber = customerNumber;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }
}
