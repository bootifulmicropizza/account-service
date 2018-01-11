package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.Payment;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for payments.
 */
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

}
