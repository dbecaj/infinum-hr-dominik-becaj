package com.example.infinum.learningandroid.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.infinum.learningandroid.MyApplication;
import com.example.infinum.learningandroid.R;

/**
 * Created by infinum on 03/08/2017.
 */

public class SystemStateProvider {

    private SystemStateProvider() {

    }

    public static boolean hasInternetConnection() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null;
    }

    public static boolean doesSupportDualPane() {
        return MyApplication.getInstance().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE &&
                MyApplication.getInstance().getResources().getBoolean(R.bool.is_tablet);
    }

}
