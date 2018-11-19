package com.yadan.saleticket.base.tools;

import org.apache.commons.math3.util.ArithmeticUtils;

import java.util.Random;

public class CommonUtils {
    public static String genRandomNumCode(int length) {
        return String.valueOf(new Random()
                .nextInt(ArithmeticUtils.pow(10, length) - ArithmeticUtils.pow(10, length - 1)) + ArithmeticUtils.pow(10, length - 1));
    }
}
