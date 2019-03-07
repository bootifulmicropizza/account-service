package com.bootifulmicropizza.service.account.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Customer entity.
 */
@Entity
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;
}
