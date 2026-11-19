package dev.backend.authservice.exception.user;


import static dev.backend.authservice.util.Constants.MESSAGE_USER_EXIST;

public class UserAlreadyExistsException extends RuntimeException {


    public UserAlreadyExistsException(String username) {
        super(MESSAGE_USER_EXIST.formatted(username));
    }
}