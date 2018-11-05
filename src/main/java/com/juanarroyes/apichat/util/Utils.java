package com.juanarroyes.apichat.util;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Slf4j
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

    /**
     *
     * @param inputDate
     * @param inputFormat
     * @return
     */
    public static Date getDateFromString(String inputDate, String inputFormat) {

        Date date = null;

        try {
            DateFormat format = new SimpleDateFormat(inputFormat);
            date = format.parse(inputDate);
        } catch (ParseException e) {
            log.error("Unexpected error in method getDateFromString", e);
        }
        return date;
    }
}
