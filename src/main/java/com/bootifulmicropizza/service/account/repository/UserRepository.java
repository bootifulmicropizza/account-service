package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for users.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);
}
