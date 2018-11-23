package com.juanarroyes.apichat.helpers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class TokenHelper {

    private static final Date STATIC_NOW = new Date();

    private static final String SECRET_KEY = "mykeysecret";

    private static final int EXPIRED_IN_MS = 360000;

    public static String generateToken(Long userId) {
        Date expiryDate = new Date(STATIC_NOW.getTime()+ EXPIRED_IN_MS);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static int getExpirationTime() {
        return EXPIRED_IN_MS;
    }

    public static String getRefreshToken() {
        return UUID.randomUUID().toString();
    }

}
