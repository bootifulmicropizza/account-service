package com.bootifulmicropizza.service.account;

import com.bootifulmicropizza.service.account.domain.*;
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

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void verifyJpaConfigurationForAccount() {
        final Address address = new Address("", "10", "High Street", "TownVille", "Northamptonshire", "AB1 2CD");

        final Payment payment =
            new Payment("123456789012345", CardType.VISA_DEBIT, LocalDate.of(2020, 05, 31), LocalDate.of(2016, 6, 1));

        final Account account =
            testEntityManager.persistFlushFind(new Account(address, Collections.singleton(payment)));

        assertThat(account.getAccountNumber(), not(isEmptyString()));

        assertThat(account.getAddress().getBuildingName(), equalTo(""));
        assertThat(account.getAddress().getBuildingNumber(), equalTo("10"));
        assertThat(account.getAddress().getStreet(), equalTo("High Street"));
        assertThat(account.getAddress().getTown(), equalTo("TownVille"));
        assertThat(account.getAddress().getCountry(), equalTo("Northamptonshire"));
        assertThat(account.getAddress().getPostCode(), equalTo("AB1 2CD"));

        assertThat(account.getPayments().iterator().next().getCardNumber(), equalTo("123456789012345"));
        assertThat(account.getPayments().iterator().next().getCardType(), equalTo(CardType.VISA_DEBIT));
        assertThat(account.getPayments().iterator().next().getStartDate(), equalTo(LocalDate.of(2016, 6, 1)));
        assertThat(account.getPayments().iterator().next().getExpiryDate(), equalTo(LocalDate.of(2020, 5, 31)));
    }

    @Test
    public void verifyJpaConfigurationForAddress() {
        final Address address = testEntityManager
            .persistFlushFind(new Address("", "10", "High Street", "TownVille", "Northamptonshire", "AB1 2CD"));

        assertThat(address.getId(), notNullValue());
        assertThat(address.getBuildingName(), equalTo(""));
        assertThat(address.getBuildingNumber(), equalTo("10"));
        assertThat(address.getStreet(), equalTo("High Street"));
        assertThat(address.getTown(), equalTo("TownVille"));
        assertThat(address.getCountry(), equalTo("Northamptonshire"));
        assertThat(address.getPostCode(), equalTo("AB1 2CD"));
    }

    @Test
    public void verifyJpaConfigurationForCustomer() {
        final Customer customer = testEntityManager
            .persistFlushFind(new Customer("Joe", "Bloggs", "joe@bloggs.com", "joe.bloggs", "password", new Account()));

        assertThat(customer.getId(), notNullValue());
        assertThat(customer.getFirstName(), equalTo("Joe"));
        assertThat(customer.getLastName(), equalTo("Bloggs"));
        assertThat(customer.getEmailAddress(), equalTo("joe@bloggs.com"));
        assertThat(customer.getUsername(), equalTo("joe.bloggs"));
        assertThat(customer.getPassword(), equalTo("password"));
    }

    @Test
    public void verifyJpaConfigurationForPayment() {
        final Payment payment = testEntityManager.persistFlushFind(
            new Payment("123456789012345", CardType.VISA_DEBIT, LocalDate.of(2020, 05, 31), LocalDate.of(2016, 6, 1)));

        assertThat(payment.getId(), notNullValue());
        assertThat(payment.getCardNumber(), equalTo("123456789012345"));
        assertThat(payment.getCardType(), equalTo(CardType.VISA_DEBIT));
        assertThat(payment.getStartDate(), equalTo(LocalDate.of(2016, 6, 1)));
        assertThat(payment.getExpiryDate(), equalTo(LocalDate.of(2020, 5, 31)));
    }
}
