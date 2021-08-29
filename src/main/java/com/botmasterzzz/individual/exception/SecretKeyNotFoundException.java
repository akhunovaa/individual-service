package com.botmasterzzz.individual.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class SecretKeyNotFoundException extends RuntimeException {

    private final static String ERROR_MESSAGE_ENG = "User application key not found";
    private final static String ERROR_MESSAGE_RUS = "Секретный ключ не найден";

    public SecretKeyNotFoundException() {
        super(ERROR_MESSAGE_ENG);
    }

    @Override
    public String getMessage() {
        return ERROR_MESSAGE_ENG;
    }
}