package com.juanarroyes.apichat.exception;

public class ApiChatException extends Exception {

    public ApiChatException(){
        super();
    }

    public ApiChatException(String message){
        super(message);
    }

    public ApiChatException(String message, Throwable cause){
        super(message, cause);
    }

    public ApiChatException(Throwable cause){
        super(cause);
    }


}
