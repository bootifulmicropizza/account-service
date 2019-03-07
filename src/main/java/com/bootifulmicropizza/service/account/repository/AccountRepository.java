package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for {@link Account}.
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, String> {

}
