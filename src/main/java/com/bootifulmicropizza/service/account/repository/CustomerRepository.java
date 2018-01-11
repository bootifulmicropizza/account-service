package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for customers.
 */
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Customer findByCustomerNumber(String customerNumber);

    Customer findByUsernameIgnoreCase(String username);
}
