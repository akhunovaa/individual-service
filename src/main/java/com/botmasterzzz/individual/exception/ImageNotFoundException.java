package com.botmasterzzz.individual.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {

    private String message;
    private String fileLocation;

    public ImageNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public ImageNotFoundException(String message, String fileLocation) {
        super(message);
        this.message = message;
        this.message = fileLocation;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
