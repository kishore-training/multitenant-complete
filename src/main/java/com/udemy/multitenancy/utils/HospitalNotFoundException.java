package com.udemy.multitenancy.utils;

public class HospitalNotFoundException extends Exception {
    public HospitalNotFoundException(String message) {
        super(message);
    }
}
