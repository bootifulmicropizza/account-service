package com.bootifulmicropizza.service.account.service;

import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.domain.User;
import com.bootifulmicropizza.service.account.domain.UserDetails;
import com.bootifulmicropizza.service.account.domain.UserRole;
import com.bootifulmicropizza.service.account.exception.CustomerNotFoundException;
import com.bootifulmicropizza.service.account.repository.CustomerRepository;
import com.bootifulmicropizza.service.account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
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

    private final UserRepository userRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public AccountUserDetailsService(final UserRepository userRepository, final CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = this.userRepository.findByUsernameIgnoreCase(username);

        if (user == null) {
            throw new UsernameNotFoundException("Unable to find username " + username);
        }

        final Customer customer = this.customerRepository.findByUser(user);

        if (customer == null) {
            throw new CustomerNotFoundException("Unable to find customer " + customer);
        }

        final Set<UserRole> userRoles = user.getRoles();

        final List<String> roles =
            userRoles.stream().map(role -> new String(role.getRole().name())).collect(Collectors.toList());

        return new UserDetails(customer.getCustomerNumber(),
                               user.getUsername(),
                               user.getPassword(),
                               true,
                               true,
                               true,
                               true,
                               AuthorityUtils.createAuthorityList(roles.toArray(new String[]{})));
    }
}
