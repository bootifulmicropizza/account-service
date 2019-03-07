package com.bootifulmicropizza.service.account.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity {

    @Id
    private String id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Payment> payments;
}
