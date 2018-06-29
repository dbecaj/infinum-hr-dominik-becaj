package com.example.infinum.learningandroid.model.storage;

import android.content.Context;
import android.content.ContextWrapper;

import com.example.infinum.learningandroid.MyApplication;

import java.io.File;

/**
 * Created by infinum on 05/08/2017.
 */

public class SystemStorage {

    private SystemStorage() {

    }

    public static File getImageDir() {
        ContextWrapper wrapper = (ContextWrapper) MyApplication.getInstance().getApplicationContext();

        return wrapper.getDir("images", Context.MODE_PRIVATE);
    }

}
