package com.wulin.utils;

import java.util.Random;

/**
 * Created by fengguoyang on 17/1/30.
 */
public class VerifyCodeGenerator {

    public static String generateNormalVerifyCodeWithSixLength(){
        String charValue = "";
        for (int i = 0; i < 6; i++){
            char c = (char) (randomInt(0,10)+'0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    private static int randomInt(int from, int to){
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
}
