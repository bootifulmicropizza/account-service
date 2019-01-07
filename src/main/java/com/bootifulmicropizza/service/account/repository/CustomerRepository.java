package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for customers.
 */
public interface CustomerRepository extends PagingAndSortingRepository<Customer, String> {

    Customer findByUser(User user);
}
