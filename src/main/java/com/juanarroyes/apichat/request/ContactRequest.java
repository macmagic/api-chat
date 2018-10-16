package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ContactRequest {

    @JsonAlias("user_request_id")
    private Long userRequestId;

    public Long getUserRequestId() {
        return userRequestId;
    }

    public void setUserRequestId(Long userRequestId) {
        this.userRequestId = userRequestId;
    }
}
