package com.botmasterzzz.individual.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class SecretKeyLimitCommitedException extends RuntimeException {

    private final static String ERROR_MESSAGE_ENG = "User application key creation limit reached";
    private final static String ERROR_MESSAGE_RUS = "Достигнут лимит по созданию ключей пользовательских приложений";

    public SecretKeyLimitCommitedException() {
        super(ERROR_MESSAGE_ENG);
    }

    @Override
    public String getMessage() {
        return ERROR_MESSAGE_ENG;
    }
}