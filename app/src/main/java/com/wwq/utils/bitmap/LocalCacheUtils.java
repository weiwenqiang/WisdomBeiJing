package com.wwq.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.wwq.utils.MD5Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by 魏文强 on 2016/10/24.
 */
public class LocalCacheUtils {
    public static final String CACHE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";

    //从本地sdcard读图片
    public Bitmap getBitmapFromLocal(String url) {
        try {
            String fileName = MD5Util.encode(url);
            File file = new File(CACHE_PATH, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    //向sdcard写图片
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Util.encode(url);
            File file = new File(CACHE_PATH, fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {// 如果文件夹不存在, 创建文件夹
                parentFile.mkdirs();
            }
            // 将图片保存在本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
