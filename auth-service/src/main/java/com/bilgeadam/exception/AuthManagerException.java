package com.bilgeadam.exception;

import lombok.Getter;

@Getter // bu anotasyonun olması yeterli
public class AuthManagerException extends  RuntimeException{ // exception sınıfı oldugu icin buran extend almalı
    private final ErrorType errorType;

    public AuthManagerException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }
    public AuthManagerException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
