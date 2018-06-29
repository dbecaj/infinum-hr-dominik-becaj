package com.example.infinum.learningandroid.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by infinum on 14/08/2017.
 */

public class AppUtils {

    protected AppUtils() {

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
