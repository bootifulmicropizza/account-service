package com.bootifulmicropizza.service.account.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Payment entity.
 */
@Entity
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity implements Serializable {

    @Id
    private String id;

    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private LocalDate expiryDate;

    private LocalDate startDate;
}
