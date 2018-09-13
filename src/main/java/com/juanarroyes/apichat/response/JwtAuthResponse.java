package com.juanarroyes.apichat.response;

public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthResponse(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String getTokenType() {
        return tokenType;
    }

    private void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
