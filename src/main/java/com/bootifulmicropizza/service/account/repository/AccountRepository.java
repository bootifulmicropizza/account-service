package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for accounts.
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Account findByAccountNumber(String accountNumber);
}
