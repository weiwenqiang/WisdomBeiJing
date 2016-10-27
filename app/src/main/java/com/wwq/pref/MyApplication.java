package com.wwq.pref;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 魏文强 on 2016/10/26.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //科大讯飞初始化
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "58104c31");
    }
}
