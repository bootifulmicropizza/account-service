package com.bootifulmicropizza.service.account.rest;

import com.bootifulmicropizza.service.account.domain.Account;
import com.bootifulmicropizza.service.account.repository.AccountRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the {code /accounts/} endpoint.
 */
@RestController
@RequestMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AccountRestController {

    private AccountRepository accountRepository;

    public AccountRestController(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PreAuthorize("(authentication.details.decodedDetails['account_id'] == #accountId) or hasRole('ADMIN')")
    @GetMapping("/{accountId}/")
    public ResponseEntity<Account> getAccount(@PathVariable final String accountId, Authentication authentication) {
        final Account account = accountRepository.findByAccountNumber(accountId);

        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(account);
    }
}
