package com.yadan.saleticket.base.tools;


import javax.sound.midi.Soundbank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by nujian on 16/3/29.
 */
public class DateUtils {


    public static void main(String[] args) {
        LocalDateTime l = LocalDateTime.now();
        System.out.println( l.minus(20, ChronoUnit.MINUTES));
    }

}

