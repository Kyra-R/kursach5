package com.example.security.util;

public class LoginAlreadyDefinedException extends RuntimeException{
    public LoginAlreadyDefinedException() {
        super("Login already defined!");
    }
}
