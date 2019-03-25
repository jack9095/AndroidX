package com.example.myapplication.app;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.Builder builder = new LogUtil.Builder(this)
                .isLog(true)
                .isLogBorder(true)
                .setLogType(LogUtil.TYPE.E)
                .setTag("fly");
        LogUtil.init(builder);
    }
}
