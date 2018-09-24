package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ContactAnswerRequest {

    @JsonAlias("user_request_response")
    private String userRequestResponse;

    public void setUserRequestResponse(String userRequestResponse) {
        this.userRequestResponse = userRequestResponse;
    }

    public String getUserRequestResponse() {
        return userRequestResponse;
    }
}
