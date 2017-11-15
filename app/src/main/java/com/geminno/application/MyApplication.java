package com.geminno.application;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xu on 2017/7/25.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }


    }
}