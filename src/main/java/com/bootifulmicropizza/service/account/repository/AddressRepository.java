package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.Address;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for addresses.
 */
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {

}
