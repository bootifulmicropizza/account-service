package com.bootifulmicropizza.service.account.rest.request;

import com.bootifulmicropizza.service.account.domain.Customer;

import java.io.Serializable;

/**
 * Class to represent the fields required to create a {@link Customer} object via a REST request.
 */
public class CreateCustomerRequest implements Serializable {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String emailAddress;

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
}
