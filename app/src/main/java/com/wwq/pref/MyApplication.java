package com.wwq.pref;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 魏文强 on 2016/10/26.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
