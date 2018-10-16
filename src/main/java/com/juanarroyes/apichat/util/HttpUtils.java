package com.juanarroyes.apichat.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

    /**
     *
     * @param request HttpServletRequest
     * @return String|null
     */
    public static String getAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
