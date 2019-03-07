package com.bootifulmicropizza.service.account.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Base entity for common data items.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseEntity {

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long lastModified;
}
