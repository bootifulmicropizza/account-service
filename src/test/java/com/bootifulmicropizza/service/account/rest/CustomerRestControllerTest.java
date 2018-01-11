package com.bootifulmicropizza.service.account.rest;

import com.bootifulmicropizza.service.account.TestUtils;
import com.bootifulmicropizza.service.account.domain.Account;
import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.repository.CustomerRepository;
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

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    @Autowired
    private TestUtils testUtils;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    private Account account = new Account();

    @Before
    public void setUp() {
        final Customer customer = new Customer("Joe", "Bloggs", "joe@bloggs.com", "joe.bloggs", "password", account);
        customer.setId(1L);

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(customerRepository.findOne(1L)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findByCustomerNumber(any(String.class))).thenReturn(customer);
    }

    @Test
    public void getAllCustomers() throws Exception {
        mockMvc.perform(get("/customers/"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("@.[0].firstName").value("Joe"))
               .andExpect(jsonPath("@.[0].lastName").value("Bloggs"))
               .andExpect(jsonPath("@.[0].emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("@.[0].username").value("joe.bloggs"))
               .andExpect(jsonPath("@.[0].password").value("password"));

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void getSingleCustomer() throws Exception {
        mockMvc.perform(get("/customers/1/"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("firstName").value("Joe"))
               .andExpect(jsonPath("lastName").value("Bloggs"))
               .andExpect(jsonPath("emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("username").value("joe.bloggs"))
               .andExpect(jsonPath("password").value("password"));

        verify(customerRepository, times(1)).findOne(1L);
    }

    @Test
    public void getSingleCustomerThatDoesNotExist() throws Exception {
        mockMvc.perform(get("/customers/2/"))
               .andExpect(status().isNotFound());

        verify(customerRepository, times(1)).findOne(2L);
    }

    @Test
    public void findSingleCustomerByCustomerNumber() throws Exception {
        mockMvc.perform(get("/customers/by-customer-number/32455456463/"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("firstName").value("Joe"))
               .andExpect(jsonPath("lastName").value("Bloggs"))
               .andExpect(jsonPath("emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("username").value("joe.bloggs"))
               .andExpect(jsonPath("password").value("password"));

        verify(customerRepository, times(1)).findByCustomerNumber("32455456463");
    }

    @Test
    public void saveSingleCustomer() throws Exception {
        final Customer customer = new Customer("Joe", "Bloggs", "joe@bloggs.com", "joe.bloggs", "password", account);

        when(customerRepository.findByUsernameIgnoreCase(any(String.class))).thenReturn(null);

        mockMvc.perform(
            post("/customers/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(testUtils.asJsonString(customer)))
               .andExpect(status().isCreated())
               .andExpect(MockMvcResultMatchers.header().string("LOCATION", "/customers/1/"))
               .andExpect(jsonPath("id").value(1L))
               .andExpect(jsonPath("firstName").value("Joe"))
               .andExpect(jsonPath("lastName").value("Bloggs"))
               .andExpect(jsonPath("emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("username").value("joe.bloggs"))
               .andExpect(jsonPath("password").value("password"));

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void updateSingleCustomer() throws Exception {
        final Customer customer = new Customer("Joe", "Bloggs", "joe@bloggs.com", "joe.bloggs", "password", account);

        when(customerRepository.findOne(1L)).thenReturn(customer);

        mockMvc.perform(
            put("/customers/1/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(testUtils.asJsonString(customer)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("id").value(1L))
               .andExpect(jsonPath("firstName").value("Joe"))
               .andExpect(jsonPath("lastName").value("Bloggs"))
               .andExpect(jsonPath("emailAddress").value("joe@bloggs.com"))
               .andExpect(jsonPath("username").value("joe.bloggs"))
               .andExpect(jsonPath("password").value("password"));

        verify(customerRepository, times(1)).findOne(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void updateSingleCustomerThatDoesNotExist() throws Exception {
        when(customerRepository.findOne(1L)).thenReturn(null);

        final Customer customer = new Customer("Joe", "Bloggs", "joe@bloggs.com", "joe.bloggs", "password", account);

        mockMvc.perform(
            put("/customers/1/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(testUtils.asJsonString(customer)))
               .andExpect(status().isNotFound());

        verify(customerRepository, times(1)).findOne(1L);
    }

    @Test
    public void deleteSingleCustomer() throws Exception {
        mockMvc.perform(delete("/customers/1/"))
               .andExpect(status().isNoContent());

        verify(customerRepository, times(1)).delete(1L);
    }
}