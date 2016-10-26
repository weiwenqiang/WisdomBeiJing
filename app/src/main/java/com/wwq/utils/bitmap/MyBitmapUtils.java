package com.wwq.utils.bitmap;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.wwq.wisdombeijing.R;

/**
 * Created by 魏文强 on 2016/10/24.
 */
public class MyBitmapUtils {
    NetCacheUtils netCacheUtils;
    LocalCacheUtils localCacheUtils;
    MemoryCacheUtils memoryCacheUtils;
    private Bitmap bitmap;

    public MyBitmapUtils() {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils();
        netCacheUtils = new NetCacheUtils(localCacheUtils, memoryCacheUtils);
    }

    public void display(ImageView ivPic, String url) {
        ivPic.setImageResource(R.drawable.news_pic_default);
        //从内存读
        bitmap = memoryCacheUtils.getBitmapFromMemory(url);
        if(bitmap != null){
            ivPic.setImageBitmap(bitmap);
            System.out.println("从内存读到图片");
            return;
        }
        //从本地读
        bitmap = localCacheUtils.getBitmapFromLocal(url);
        if(bitmap != null){
            ivPic.setImageBitmap(bitmap);
            System.out.println("从本地读图片");
            return;
        }
        //从网络读
        netCacheUtils.getBitmapFromNet(ivPic, url);
    }
}
