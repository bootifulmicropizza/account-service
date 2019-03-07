package com.bootifulmicropizza.service.account.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccountRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}