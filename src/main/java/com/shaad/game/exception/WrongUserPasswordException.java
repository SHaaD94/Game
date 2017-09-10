package com.shaad.game.exception;

public class WrongUserPasswordException extends RuntimeException {
    public WrongUserPasswordException() {
        super("Wrong password");
    }
}
