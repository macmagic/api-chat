package com.juanarroyes.apichat.util;

import java.util.Random;

public class Utils {

    public static String generateRandomString(int length) {
        if(length == 0) {
            length = 20;
        }

        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return String.valueOf(array);
    }
}
