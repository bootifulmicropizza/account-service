package com.bootifulmicropizza.service.account.repository;

import com.bootifulmicropizza.service.account.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;

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

    private static final String CUSTOMER_NUMBER = "12345";

    @Autowired
    private CustomerRepository customerRepository;

    private User user;

    @Before
    public void setUp() {
        user = new User("joe.bloggs", "password", Collections.singleton(new UserRole(Role.ROLE_CUSTOMER)));

        final Address address = new Address("", "10", "High Street", "TownVille", "Northamptonshire", "AB1 2CD");

        final Payment payment = new Payment("123456789012345", CardType.VISA_DEBIT, LocalDate.of(2020, 5, 31), LocalDate.of(2016, 6, 1));

        final Customer customer = new Customer(user, "Joe", "Bloggs", "joe@bloggs.com", address, Collections.singleton(payment));
        customer.setCustomerNumber(CUSTOMER_NUMBER);

        Customer savedCustomer = customerRepository.save(customer);
        user = savedCustomer.getUser();
    }

    @Test
    public void findSingleCustomerByCustomerNumber() {
        final Customer customer = customerRepository.findOne(CUSTOMER_NUMBER);

        assertThat(customer, notNullValue());
        assertThat(customer.getCustomerNumber(), notNullValue());
        assertThat(customer.getCustomerNumber(), equalTo(CUSTOMER_NUMBER));
        assertThat(customer.getFirstName(), equalTo("Joe"));
        assertThat(customer.getLastName(), equalTo("Bloggs"));
        assertThat(customer.getEmailAddress(), equalTo("joe@bloggs.com"));
        assertThat(customer.getAddress(), notNullValue());
        assertThat(customer.getAddress().getBuildingName(), equalTo(""));
        assertThat(customer.getAddress().getBuildingNumber(), equalTo("10"));
        assertThat(customer.getAddress().getStreet(), equalTo("High Street"));
        assertThat(customer.getAddress().getTown(), equalTo("TownVille"));
        assertThat(customer.getAddress().getCounty(), equalTo("Northamptonshire"));
        assertThat(customer.getAddress().getPostCode(), equalTo("AB1 2CD"));
    }

    @Test
    public void findSingleCustomerByUsername() {
        final Customer customer = customerRepository.findByUser(user);

        assertThat(customer, notNullValue());
        assertThat(customer.getCustomerNumber(), notNullValue());
        assertThat(customer.getFirstName(), equalTo("Joe"));
        assertThat(customer.getLastName(), equalTo("Bloggs"));
        assertThat(customer.getEmailAddress(), equalTo("joe@bloggs.com"));
        assertThat(customer.getAddress(), notNullValue());
        assertThat(customer.getAddress().getBuildingName(), equalTo(""));
        assertThat(customer.getAddress().getBuildingNumber(), equalTo("10"));
        assertThat(customer.getAddress().getStreet(), equalTo("High Street"));
        assertThat(customer.getAddress().getTown(), equalTo("TownVille"));
        assertThat(customer.getAddress().getCounty(), equalTo("Northamptonshire"));
        assertThat(customer.getAddress().getPostCode(), equalTo("AB1 2CD"));
    }
}