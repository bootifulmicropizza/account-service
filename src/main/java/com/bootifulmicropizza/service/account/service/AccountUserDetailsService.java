package com.bootifulmicropizza.service.account.service;

import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.domain.User;
import com.bootifulmicropizza.service.account.domain.UserRole;
import com.bootifulmicropizza.service.account.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link UserDetailsService} implementation to provide the user details for a given username.
 */
@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Autowired
    public AccountUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Customer customer = this.customerRepository.findByUsernameIgnoreCase(username);

        if (customer == null) {
            throw new UsernameNotFoundException("Unable to find username " + username);
        }

        final Set<UserRole> userRoles = customer.getRoles();

        final List<String> roles =
            userRoles.stream().map(role -> new String(role.getRole().name())).collect(Collectors.toList());

        return new User(customer.getAccount().getAccountNumber(),
                        customer.getUsername(),
                        customer.getPassword(),
                        customer.isActive(),
                        customer.isActive(),
                        customer.isActive(),
                        customer.isActive(),
                        AuthorityUtils.createAuthorityList(roles.toArray(new String[]{})));
    }
}
