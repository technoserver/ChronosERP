package com.chronos.service;

import org.springframework.security.core.AuthenticationException;

public class AutenticationException extends AuthenticationException {

    public AutenticationException(String msg) {
        super(msg);
    }
}
