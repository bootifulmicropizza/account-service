package com.bootifulmicropizza.service.account.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Account entity.
 */
@Entity
public class Account extends BaseEntity implements Serializable {

    private Long id;

    private String accountNumber;

    private Address address;

    private Set<Payment> payments = new HashSet<>();

    public Account() {

    }

    public Account(final Address address, final Set<Payment> payments) {
        this.accountNumber = UUID.randomUUID().toString();
        this.address = address;
        this.payments = payments;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
