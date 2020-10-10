package com.udemy.multitenancy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.TimeZone;

@Document(collection = "Hospital")
public class Hospital {

    @Id
    private String id;

    private String email;

    private String hospitalName;

    private String mobileNumber;

    private String timeZone;


    private Instant createDateTime;

    private Instant updateDateTime;


    private String country;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }


    public Instant getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Instant createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Instant getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Instant updateDateTime) {
        this.updateDateTime = updateDateTime;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
