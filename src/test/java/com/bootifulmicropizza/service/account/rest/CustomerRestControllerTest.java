package com.bootifulmicropizza.service.account.rest;

import com.bootifulmicropizza.service.account.TestUtils;
import com.bootifulmicropizza.service.account.domain.*;
import com.bootifulmicropizza.service.account.repository.CustomerRepository;
import com.bootifulmicropizza.service.account.rest.request.CreateCustomerRequest;
import com.bootifulmicropizza.service.account.rest.request.UpdateCustomerRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class to test the {@link CustomerRestController} with security disabled.
 */
@WebMvcTest(controllers = CustomerRestController.class, secure = false)
@Import(TestUtils.class)
@AutoConfigureDataJpa
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
public class CustomerRestControllerTest {

    private static final String CUSTOMER_NUMBER = "12345";

    @Autowired
    private TestUtils testUtils;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    private Address address;

    private Payment payment;

    private Customer customer;

    @Before
    public void setUp() {
        user = new User("joe.bloggs", "password", Collections.singleton(new UserRole(Role.ROLE_CUSTOMER)));

        address = new Address("", "10", "High Street", "TownVille", "Northamptonshire", "AB1 2CD");

        payment =
            new Payment("123456789012345", CardType.VISA_DEBIT, LocalDate.of(2020, 5, 31), LocalDate.of(2016, 6, 1));

        customer = new Customer(user, "Joe", "Bloggs", "joe@bloggs.com", address, Collections.singleton(payment));
        customer.setCustomerNumber(CUSTOMER_NUMBER);

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findOne(CUSTOMER_NUMBER)).thenReturn(customer);
    }

    @Test
    public void getAllCustomers() throws Exception {
        mockMvc.perform(get("/customers/"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("@.[0].firstName").value("Joe"))
               .andExpect(jsonPath("@.[0].lastName").value("Bloggs"))
               .andExpect(jsonPath("@.[0].emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("@.[0].address.buildingName").value(""))
               .andExpect(jsonPath("@.[0].address.buildingNumber").value("10"))
               .andExpect(jsonPath("@.[0].address.street").value("High Street"))
               .andExpect(jsonPath("@.[0].address.town").value("TownVille"))
               .andExpect(jsonPath("@.[0].address.county").value("Northamptonshire"))
               .andExpect(jsonPath("@.[0].address.postCode").value("AB1 2CD"))
               .andExpect(jsonPath("@.[0].payments[0].cardNumber").value("123456789012345"))
               .andExpect(jsonPath("@.[0].payments[0].cardType").value(CardType.VISA_DEBIT.name()))
               .andExpect(jsonPath("@.[0].payments[0].expiryDate").value("2020-05-31"))
               .andExpect(jsonPath("@.[0].payments[0].startDate").value("2016-06-01"));

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void getSingleCustomerThatDoesNotExist() throws Exception {
        mockMvc.perform(get("/customers/11111/"))
               .andExpect(status().isNotFound());

        verify(customerRepository, times(1)).findOne("11111");
    }

    @Test
    public void findSingleCustomerByCustomerNumber() throws Exception {
        mockMvc.perform(get("/customers/" + CUSTOMER_NUMBER + "/"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("customerNumber").value(CUSTOMER_NUMBER))
               .andExpect(jsonPath("firstName").value("Joe"))
               .andExpect(jsonPath("lastName").value("Bloggs"))
               .andExpect(jsonPath("emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("address.buildingName").value(""))
               .andExpect(jsonPath("address.buildingNumber").value("10"))
               .andExpect(jsonPath("address.street").value("High Street"))
               .andExpect(jsonPath("address.town").value("TownVille"))
               .andExpect(jsonPath("address.county").value("Northamptonshire"))
               .andExpect(jsonPath("address.postCode").value("AB1 2CD"))
               .andExpect(jsonPath("payments[0].cardNumber").value("123456789012345"))
               .andExpect(jsonPath("payments[0].cardType").value(CardType.VISA_DEBIT.name()))
               .andExpect(jsonPath("payments[0].expiryDate").value("2020-05-31"))
               .andExpect(jsonPath("payments[0].startDate").value("2016-06-01"));

        verify(customerRepository, times(1)).findOne(CUSTOMER_NUMBER);
    }

    @Test
    public void saveSingleCustomer() throws Exception {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setFirstName("Joe");
        createCustomerRequest.setLastName("Bloggs");
        createCustomerRequest.setEmailAddress("joe@bloggs.com");
        createCustomerRequest.setUsername("joebloggs");
        createCustomerRequest.setPassword("password");
        createCustomerRequest.setAddress(address);
        createCustomerRequest.setPayments(Collections.singleton(payment));

        when(customerRepository.findOne(any(String.class))).thenReturn(null);


        mockMvc.perform(
            post("/customers/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(testUtils.asJsonString(createCustomerRequest)))
               .andExpect(status().isCreated())
               .andExpect(MockMvcResultMatchers.header().string("LOCATION", "/customers/" + CUSTOMER_NUMBER + "/"))
               .andExpect(jsonPath("customerNumber").value(CUSTOMER_NUMBER))
               .andExpect(jsonPath("firstName").value("Joe"))
               .andExpect(jsonPath("lastName").value("Bloggs"))
               .andExpect(jsonPath("emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("address.buildingName").value(""))
               .andExpect(jsonPath("address.buildingNumber").value("10"))
               .andExpect(jsonPath("address.street").value("High Street"))
               .andExpect(jsonPath("address.town").value("TownVille"))
               .andExpect(jsonPath("address.county").value("Northamptonshire"))
               .andExpect(jsonPath("address.postCode").value("AB1 2CD"))
               .andExpect(jsonPath("payments[0].cardNumber").value("123456789012345"))
               .andExpect(jsonPath("payments[0].cardType").value(CardType.VISA_DEBIT.name()))
               .andExpect(jsonPath("payments[0].expiryDate").value("2020-05-31"))
               .andExpect(jsonPath("payments[0].startDate").value("2016-06-01"));

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void updateSingleCustomer() throws Exception {
        when(customerRepository.findOne(CUSTOMER_NUMBER)).thenReturn(customer);

        mockMvc.perform(
            put("/customers/" + CUSTOMER_NUMBER + "/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(testUtils.asJsonString(customer)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("customerNumber").value(CUSTOMER_NUMBER))
               .andExpect(jsonPath("firstName").value("Joe"))
               .andExpect(jsonPath("lastName").value("Bloggs"))
               .andExpect(jsonPath("emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("address.buildingName").value(""))
               .andExpect(jsonPath("address.buildingNumber").value("10"))
               .andExpect(jsonPath("address.street").value("High Street"))
               .andExpect(jsonPath("address.town").value("TownVille"))
               .andExpect(jsonPath("address.county").value("Northamptonshire"))
               .andExpect(jsonPath("address.postCode").value("AB1 2CD"))
               .andExpect(jsonPath("payments[0].cardNumber").value("123456789012345"))
               .andExpect(jsonPath("payments[0].cardType").value(CardType.VISA_DEBIT.name()))
               .andExpect(jsonPath("payments[0].expiryDate").value("2020-05-31"))
               .andExpect(jsonPath("payments[0].startDate").value("2016-06-01"));

        verify(customerRepository, times(1)).findOne(CUSTOMER_NUMBER);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void updateSingleCustomerThatDoesNotExist() throws Exception {
        final UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setFirstName("Joe");
        request.setLastName("Bloggs");
        request.setEmailAddress("joe@bloggs.com");
        request.setAddress(address);
        request.setPayments(Collections.singleton(payment));

        mockMvc.perform(
            put("/customers/11111/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(testUtils.asJsonString(request)))
               .andExpect(status().isNotFound());

        verify(customerRepository, times(1)).findOne("11111");
    }

    @Test
    public void deleteSingleCustomer() throws Exception {
        mockMvc.perform(delete("/customers/" + CUSTOMER_NUMBER + "/"))
               .andExpect(status().isNoContent());

        verify(customerRepository, times(1)).delete(CUSTOMER_NUMBER);
    }
}