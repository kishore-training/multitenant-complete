package com.udemy.multitenancy.utils;

public class HospitalExistsException extends Exception{
    public HospitalExistsException(String message) {
        super(message);
    }
}
