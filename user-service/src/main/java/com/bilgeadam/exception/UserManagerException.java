package com.bilgeadam.exception;

import lombok.Getter;

@Getter // bu anotasyonun olması yeterli
public class UserManagerException extends  RuntimeException{ // exception sınıfı oldugu icin buran extend almalı
    private final ErrorType errorType;

    public UserManagerException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }
    public UserManagerException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
