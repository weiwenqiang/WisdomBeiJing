package com.wwq.utils;

import android.content.Context;

/**
 * Created by 魏文强 on 2016/10/23.
 */
public class CacheUtils {
    /**
     * 设置缓存
     */
    public static void setCache(Context context, String key, String json){
        SPUtils.put(context, key, json);
    }

    /**
     * 获取缓存
     */
    public static String getCache(Context context, String key){
        return (String) SPUtils.get(context, key, "");
    }
}
