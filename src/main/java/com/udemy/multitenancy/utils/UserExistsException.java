package com.udemy.multitenancy.utils;

public class UserExistsException extends Exception {

    public UserExistsException(String message){
        super(message);
    }
}
