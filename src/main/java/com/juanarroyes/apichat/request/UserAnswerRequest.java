package com.juanarroyes.apichat.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UserAnswerRequest {

    @JsonAlias("request_id")
    private Long requestId;

    @JsonAlias("user_request_response")
    private String userRequestResponse;

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setUserRequestResponse(String userRequestResponse) {
        this.userRequestResponse = userRequestResponse;
    }

    public String getUserRequestResponse() {
        return userRequestResponse;
    }
}
