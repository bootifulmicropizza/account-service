package com.bootifulmicropizza.service.account.rest;

import com.bootifulmicropizza.service.account.TestUtils;
import com.bootifulmicropizza.service.account.domain.Account;
import com.bootifulmicropizza.service.account.domain.Customer;
import com.bootifulmicropizza.service.account.repository.AccountRepository;
import com.bootifulmicropizza.service.account.rest.request.CreateAccountRequest;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountRestController.class, secure=false)
@AutoConfigureDataJpa
@Import(TestUtils.class)
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
public class AccountRestControllerTest {

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        final Customer customer = new Customer(UUID.randomUUID().toString(), "Joe", "Bloggs", "joe@bloggs.com");
        final Account account = new Account(UUID.randomUUID().toString(), customer, null, null);

        when(accountRepository.save(any(Account.class))).thenReturn(account);
    }

    @Test
    public void testRegisterEndpoint() throws Exception {
        final CreateAccountRequest request = new CreateAccountRequest("Joe", "Bloggs", "joe@bloggs.com", "letmein");

        mockMvc.perform(
            post("/account/")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(testUtils.asJsonString(request))
                .with(csrf()))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath(".customer.firstName").value(request.getFirstName()));
    }
}
