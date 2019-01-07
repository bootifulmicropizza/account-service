package com.bootifulmicropizza.service.account.rest;

import com.bootifulmicropizza.service.account.domain.*;
import com.bootifulmicropizza.service.account.repository.CustomerRepository;
import com.bootifulmicropizza.service.account.repository.UserRepository;
import com.bootifulmicropizza.service.account.rest.request.CreateCustomerRequest;
import com.bootifulmicropizza.service.account.rest.request.UpdateCustomerRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * REST controller for the {code /customers/} endpoint.
 */
@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerRestController {

    private UserRepository userRepository;

    private CustomerRepository customerRepository;

    private PasswordEncoder bCryptPasswordEncoder;

    public CustomerRestController(final UserRepository userRepository,
                                  final CustomerRepository customerRepository,
                                  final PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACCOUNT_READ')")
    @GetMapping("/")
    public ResponseEntity<List<Customer>> getCustomers() {
        final List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customer -> customers.add(customer));

        return ResponseEntity.ok(customers);
    }

    @PreAuthorize("(authentication.details.decodedDetails['customer_number'] == #customerNumber) or hasRole('ADMIN') or hasAuthority('ACCOUNT_READ')")
    @GetMapping("/{customerNumber}/")
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerNumber") final String customerNumber) {
        final Customer customer = customerRepository.findOne(customerNumber);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACCOUNT_WRITE')")
    @PostMapping("/")
    public ResponseEntity<Customer> createCustomer(@RequestBody final CreateCustomerRequest request) {
        Assert.notNull(request.getFirstName(), "First name is required");
        Assert.notNull(request.getLastName(), "Last name is required");
        Assert.notNull(request.getEmailAddress(), "Email address is required");
        Assert.notNull(request.getAddress(), "Address is required");
        Assert.notNull(request.getPayments(), "Payments are required");

        if (userAlreadyExists(request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        final User user = new User(request.getUsername(), bCryptPasswordEncoder.encode(request.getPassword()),
                             Collections.singleton(new UserRole(Role.ROLE_CUSTOMER)));

        final Customer customer =
            new Customer(user, request.getFirstName(), request.getLastName(), request.getEmailAddress(), new Address(),
                         Collections.emptySet());

        final Customer savedCustomer = customerRepository.save(customer);

        return ResponseEntity.created(URI.create("/customers/" + savedCustomer.getCustomerNumber() + "/")).body(savedCustomer);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACCOUNT_WRITE')")
    @PutMapping("/{customerNumber}/")
    public ResponseEntity<Customer> updateCustomer(@RequestBody final UpdateCustomerRequest request,
                                                   @PathVariable("customerNumber") final String customerNumber) {
        Assert.notNull(request.getFirstName(), "First name is required");
        Assert.notNull(request.getLastName(), "Last name is required");
        Assert.notNull(request.getEmailAddress(), "Email address is required");

        final Customer customer = customerRepository.findOne(customerNumber);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmailAddress(request.getEmailAddress());

        final Customer updatedCustomer = customerRepository.save(customer);

        return ResponseEntity.ok().body(updatedCustomer);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACCOUNT_WRITE')")
    @DeleteMapping("/{customerNumber}/")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable final String customerNumber) {
        customerRepository.delete(customerNumber);

        return ResponseEntity.noContent().build();
    }

    private boolean userAlreadyExists(final String username) {
        return userRepository.findByUsernameIgnoreCase(username) != null;
    }
}
