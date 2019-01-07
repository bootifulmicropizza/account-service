package com.bootifulmicropizza.service.account.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Address entity.
 */
@Entity
public class Address extends BaseEntity implements Serializable {

    private Long id;

    private String buildingNumber;

    private String buildingName;

    private String street;

    private String town;

    private String county;

    private String postCode;

    public Address() {

    }

    public Address(String buildingName, String buildingNumber, String street, String town, String county,
                   String postCode) {
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
        this.street = street;
        this.town = town;
        this.county = county;
        this.postCode = postCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
