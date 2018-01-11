package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.Account;
import com.bootifulmicropizza.service.account.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test to test the {@link CustomerRepository} class.
 */
@DataJpaTest
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findSingleCustomerByCustomerNumber() {
        final Customer newCustomer =
            customerRepository.save(new Customer("Joe", "Bloggs", "joe@bloggs.com", "joe.bloggs", "password", new Account()));

        final Customer customer = customerRepository.findByCustomerNumber(newCustomer.getCustomerNumber());

        assertThat(customer, notNullValue());
        assertThat(customer.getId(), notNullValue());
        assertThat(customer.getFirstName(), equalTo("Joe"));
        assertThat(customer.getLastName(), equalTo("Bloggs"));
        assertThat(customer.getEmailAddress(), equalTo("joe@bloggs.com"));
        assertThat(customer.getUsername(), equalTo("joe.bloggs"));
        assertThat(customer.getPassword(), equalTo("password"));
    }

    @Test
    public void findSingleCustomerByUsername() {
        final Customer newCustomer =
            customerRepository.save(new Customer("Joe", "Bloggs", "joe@bloggs.com", "joe.bloggs", "password", new Account()));

        final Customer customer = customerRepository.findByUsernameIgnoreCase("JoE.BLogGs");

        assertThat(customer, notNullValue());
        assertThat(customer.getId(), notNullValue());
        assertThat(customer.getFirstName(), equalTo("Joe"));
        assertThat(customer.getLastName(), equalTo("Bloggs"));
        assertThat(customer.getEmailAddress(), equalTo("joe@bloggs.com"));
        assertThat(customer.getUsername(), equalTo("joe.bloggs"));
        assertThat(customer.getPassword(), equalTo("password"));
    }
}