package com.juanarroyes.apichat.exception;

/**
 * This exception is used to catch when user is not allowed to make a any action related for business design
 *
 * @author jarroyes
 */
public class UserNotAllowedException extends ApiChatException {

    public UserNotAllowedException() {

    }

    /**
     *
     * @param message Message error to send in exception
     */
    public UserNotAllowedException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public UserNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotAllowedException(Throwable cause) {
        super(cause);
    }
}
