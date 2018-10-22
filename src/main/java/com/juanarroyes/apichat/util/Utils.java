package com.juanarroyes.apichat.util;

import java.util.Random;

public class Utils {

    private static final int DEFAULT_LENGHT = 20;

    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        if(length == 0) {
            length = DEFAULT_LENGHT;
        }

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i<length; i++) {
            int character = new Random().nextInt(characters.length());
            builder.append(characters.charAt(character));
        }
        return builder.toString();
    }
}
