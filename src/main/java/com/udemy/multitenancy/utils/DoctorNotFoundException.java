package com.udemy.multitenancy.utils;

public class DoctorNotFoundException extends Exception {
    public DoctorNotFoundException(String message){
        super(message);
    }
}
