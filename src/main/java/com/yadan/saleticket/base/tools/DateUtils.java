package com.yadan.saleticket.base.tools;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 */
public class DateUtils {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime dateTime) {
        return DateUtils.format(dateTime, formatter);
    }

    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }


    public static void main(String[] args) {
        LocalDateTime l = LocalDateTime.now();
        System.out.println( l.minus(20, ChronoUnit.MINUTES));
    }

}

