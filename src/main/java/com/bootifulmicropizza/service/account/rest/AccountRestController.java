package com.bootifulmicropizza.service.account.rest;

import com.bootifulmicropizza.service.account.domain.Account;
import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.repository.AccountRepository;
import com.bootifulmicropizza.service.account.rest.request.CreateAccountRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for the {@code /account/} endpoint.
 */
@RestController
@RequestMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AccountRestController {

    private AccountRepository accountRepository;

    public AccountRestController(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping(value = "/")
    public ResponseEntity<Account> createAccount(@RequestBody final CreateAccountRequest request) {
        final Customer customer = new Customer(UUID.randomUUID().toString(), request.getFirstName(), request.getLastName(), request.getEmail());
        final Account account = new Account(UUID.randomUUID().toString(), customer, null, null);

        final Account newAccount = accountRepository.save(account);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(newAccount);
    }
}
