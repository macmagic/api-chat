package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ContactRequest {

    @JsonAlias("user_request_id")
    private Integer userRequestId;

    @JsonAlias("user_required_id")
    private Integer userRequiredId;

    public Integer getUserRequestId() {
        return userRequestId;
    }
}
