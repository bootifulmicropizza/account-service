package com.bootifulmicropizza.service.account.rest;

import com.bootifulmicropizza.service.account.domain.Account;
import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.repository.CustomerRepository;
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
import java.util.List;

/**
 * REST controller for the {code /customers/} endpoint.
 */
@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerRestController {

    private CustomerRepository customerRepository;

    private PasswordEncoder bCryptPasswordEncoder;

    public CustomerRestController(final CustomerRepository customerRepository,
                                  final PasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @GetMapping("/")
    public ResponseEntity<List<Customer>> getCustomers() {
        final List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customer -> customers.add(customer));

        return ResponseEntity.ok(customers);
    }

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @GetMapping("/{id}/")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") final Long id) {
        final Customer customer = customerRepository.findOne(id);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @GetMapping("/by-customer-number/{customerNumber}/")
    public ResponseEntity<Customer> getCustomerByCustomerNumber(@PathVariable("customerNumber") final String customerNumber) {
        final Customer customer = customerRepository.findByCustomerNumber(customerNumber);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasAuthority('ACCOUNT_WRITE')")
    @PostMapping("/")
    public ResponseEntity<Customer> createCustomer(@RequestBody final CreateCustomerRequest request) {
        Assert.notNull(request.getFirstName(), "First name is required");
        Assert.notNull(request.getLastName(), "Last name is required");
        Assert.notNull(request.getEmailAddress(), "Email address is required");
        Assert.notNull(request.getUsername(), "Username is required");
        Assert.notNull(request.getPassword(), "Password is required");

        if (userAlreadyExists(request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        final Account account = new Account();

        final Customer customer = new Customer(request.getFirstName(), request.getLastName(), request.getEmailAddress(),
                                         request.getUsername(), bCryptPasswordEncoder.encode(request.getPassword()), account);

        final Customer savedCustomer = customerRepository.save(customer);

        return ResponseEntity
            .created(URI.create("/customers/" + savedCustomer.getId() + "/"))
            .body(savedCustomer);
    }

    @PreAuthorize("hasAuthority('ACCOUNT_WRITE')")
    @PutMapping("/{id}/")
    public ResponseEntity<Customer> updateCustomer(@RequestBody final UpdateCustomerRequest request,
                                                   @PathVariable("id") final Long id) {
        Assert.notNull(request.getFirstName(), "First name is required");
        Assert.notNull(request.getLastName(), "Last name is required");
        Assert.notNull(request.getEmailAddress(), "Email address is required");

        final Customer customer = customerRepository.findOne(id);

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmailAddress(request.getEmailAddress());

        final Customer updatedCustomer = customerRepository.save(customer);

        return ResponseEntity.ok().body(updatedCustomer);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable final Long id) {
        customerRepository.delete(id);

        return ResponseEntity.noContent().build();
    }

    private boolean userAlreadyExists(final String username) {
        return customerRepository.findByUsernameIgnoreCase(username) != null;
    }
}
