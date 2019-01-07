package com.bootifulmicropizza.service.account;

import com.bootifulmicropizza.service.account.domain.*;
import com.bootifulmicropizza.service.account.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return mapper;
    }
}

@Component
class TestDataCLR implements CommandLineRunner {

    private CustomerRepository customerRepository;

    private PasswordEncoder passwordEncoder;

    public TestDataCLR(final CustomerRepository customerRepository, final PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... strings) {
        if (customerRepository.count() == 0) {
            populateDatabase();

            customerRepository.findAll().forEach(System.out::println);
        }
    }

    private void populateDatabase() {
        final User user = new User("iancollington", passwordEncoder.encode("password"),
                                   Collections.singleton(new UserRole(Role.ROLE_CUSTOMER)));

        final Address address = new Address("", "10", "High Street", "TownVille", "Northamptonshire", "AB1 2CD");

        LocalDate expiryDate = LocalDate.of(2020, 11, 30);
        LocalDate startDate = LocalDate.of(2017, 11, 30);
        Payment payment = new Payment("12334567890", CardType.VISA, expiryDate, startDate);
        customerRepository
            .save(new Customer(user, "Ian", "Collington", "iancollington@gmail.com", address, Collections.singleton(payment)));
    }
}