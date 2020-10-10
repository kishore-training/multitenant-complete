package com.udemy.multitenancy.utils;

public class AppointmentExistsException extends Exception {

    public AppointmentExistsException(String message){
        super(message);
    }
}
