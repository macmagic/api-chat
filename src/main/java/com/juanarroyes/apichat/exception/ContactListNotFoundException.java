package com.juanarroyes.apichat.exception;

public class ContactListNotFoundException extends ApiChatException {

    public ContactListNotFoundException() {

    }

    public ContactListNotFoundException(String message) {
        super(message);
    }

    public ContactListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContactListNotFoundException(Throwable cause) {
        super(cause);
    }
}
