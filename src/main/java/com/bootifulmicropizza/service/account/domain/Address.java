package com.bootifulmicropizza.service.account.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Address entity.
 */
@Entity
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseEntity implements Serializable {

    @Id
    private String id;

    private String buildingNumber;

    private String buildingName;

    private String street;

    private String town;

    private String county;

    private String postCode;
}
