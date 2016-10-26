package com.wwq.utils.bitmap;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by 魏文强 on 2016/10/25.
 */
public class MemoryCacheUtils {
    //2.3+ 被废弃
//    private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
    private LruCache<String, Bitmap> mMemoryCache;//最少最近算法

    public MemoryCacheUtils() {
        long maxSize = Runtime.getRuntime().maxMemory() / 8;
        mMemoryCache = new LruCache<String, Bitmap>((int) maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getRowBytes() * value.getHeight();
                return byteCount;
            }
        };//超过最大值，系统自动回收
    }

    //从内存读
    public Bitmap getBitmapFromMemory(String url){
//        SoftReference<Bitmap> softReference = mMemoryCache.get(url);//软引用，解决内存移除很好的办法
//        if(softReference != null){
//            Bitmap bitmap = softReference.get();
//            return bitmap;
//        }
        return mMemoryCache.get(url);

    }
    //写内存
    public void setBitmapToMemory(String url, Bitmap bitmap){
//        SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
//        mMemoryCache.put(url, softReference);
        mMemoryCache.put(url, bitmap);
    }
}
