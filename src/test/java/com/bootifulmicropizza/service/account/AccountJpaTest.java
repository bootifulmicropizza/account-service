package com.bootifulmicropizza.service.account;

import com.bootifulmicropizza.service.account.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.IsNot.not;

/**
 * Test to verify JPA configuration.
 */
@DataJpaTest
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
public class AccountJpaTest {

    private User user;

    private Address address;

    private Payment payment;

    private Customer customer;

    @Autowired
    private TestEntityManager testEntityManager;

    @Before
    public void setUp() {
        user = new User("joe.bloggs", "password", Collections.singleton(new UserRole(Role.ROLE_CUSTOMER)));

        address = new Address("", "10", "High Street", "TownVille", "Northamptonshire", "AB1 2CD");

        payment =
            new Payment("123456789012345", CardType.VISA_DEBIT, LocalDate.of(2020, 5, 31), LocalDate.of(2016, 6, 1));

        customer = new Customer(user, "Joe", "Bloggs", "joe@bloggs.com", address, Collections.singleton(payment));
    }

    @Test
    public void verifyJpaConfigurationForAccount() {
        final Customer savedCustomer = testEntityManager.persistFlushFind(customer);

        assertThat(savedCustomer.getCustomerNumber(), not(isEmptyString()));

        assertThat(savedCustomer.getAddress().getBuildingName(), equalTo(""));
        assertThat(savedCustomer.getAddress().getBuildingNumber(), equalTo("10"));
        assertThat(savedCustomer.getAddress().getStreet(), equalTo("High Street"));
        assertThat(savedCustomer.getAddress().getTown(), equalTo("TownVille"));
        assertThat(savedCustomer.getAddress().getCounty(), equalTo("Northamptonshire"));
        assertThat(savedCustomer.getAddress().getPostCode(), equalTo("AB1 2CD"));

        assertThat(savedCustomer.getPayments().iterator().next().getCardNumber(), equalTo("123456789012345"));
        assertThat(savedCustomer.getPayments().iterator().next().getCardType(), equalTo(CardType.VISA_DEBIT));
        assertThat(savedCustomer.getPayments().iterator().next().getStartDate(), equalTo(LocalDate.of(2016, 6, 1)));
        assertThat(savedCustomer.getPayments().iterator().next().getExpiryDate(), equalTo(LocalDate.of(2020, 5, 31)));
    }

    @Test
    public void verifyJpaConfigurationForAddress() {
        final Address savedAddress = testEntityManager.persistFlushFind(address);

        assertThat(savedAddress.getId(), notNullValue());
        assertThat(savedAddress.getBuildingName(), equalTo(""));
        assertThat(savedAddress.getBuildingNumber(), equalTo("10"));
        assertThat(savedAddress.getStreet(), equalTo("High Street"));
        assertThat(savedAddress.getTown(), equalTo("TownVille"));
        assertThat(savedAddress.getCounty(), equalTo("Northamptonshire"));
        assertThat(savedAddress.getPostCode(), equalTo("AB1 2CD"));
    }

    @Test
    public void verifyJpaConfigurationForCustomer() {
        final Customer savedCustomer = testEntityManager.persistFlushFind(customer);

        assertThat(savedCustomer.getCustomerNumber(), notNullValue());
        assertThat(savedCustomer.getFirstName(), equalTo("Joe"));
        assertThat(savedCustomer.getLastName(), equalTo("Bloggs"));
        assertThat(savedCustomer.getEmailAddress(), equalTo("joe@bloggs.com"));
        assertThat(savedCustomer.getUser().getUsername(), equalTo("joe.bloggs"));
        assertThat(savedCustomer.getUser().getPassword(), equalTo("password"));
    }

    @Test
    public void verifyJpaConfigurationForPayment() {
        final Payment savedPayment = testEntityManager.persistFlushFind(payment);

        assertThat(savedPayment.getId(), notNullValue());
        assertThat(savedPayment.getCardNumber(), equalTo("123456789012345"));
        assertThat(savedPayment.getCardType(), equalTo(CardType.VISA_DEBIT));
        assertThat(savedPayment.getStartDate(), equalTo(LocalDate.of(2016, 6, 1)));
        assertThat(savedPayment.getExpiryDate(), equalTo(LocalDate.of(2020, 5, 31)));
    }
}
