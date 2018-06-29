package com.example.infinum.learningandroid.utils;

import com.example.infinum.learningandroid.MyApplication;

/**
 * Created by infinum on 02/08/2017.
 */

public class ResourceProvider {

    private ResourceProvider() {

    }

    public static String getString(int resId) {
        String result = MyApplication.getInstance().getString(resId);

        return result;
    }

    public static int getInt(int resId) {
        int result = MyApplication.getInstance().getResources().getInteger(resId);

        return result;
    }

}
