package com.bootifulmicropizza.service.account.rest.request;

import com.bootifulmicropizza.service.account.domain.Address;
import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.domain.Payment;

import java.io.Serializable;
import java.util.Set;

/**
 * Class to represent the fields required to create a {@link Customer} object via a REST request.
 */
public class CreateCustomerRequest implements Serializable {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private Address address;

    private Set<Payment> payments;

    public CreateCustomerRequest() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
