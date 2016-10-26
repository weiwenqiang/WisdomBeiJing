package com.wwq.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 魏文强 on 2016/10/24.
 */
public class NetCacheUtils {

    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.localCacheUtils = localCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 从网络下载图片
     */
    public void getBitmapFromNet(ImageView ivPic, String url) {
        new BitmapTask().execute(ivPic, url);//启动AsyncTask
    }

    /**
     * AsyncTask 就是 线程池 和 Handler 的封装
     * 第一个泛型: 参数类型 第二个泛型: 更新进度的泛型, 第三个泛型是onPostExecute的返回结果
     */
    class BitmapTask extends AsyncTask<Object, Void, Bitmap>{

        private ImageView imageView;
        private String url;

        //后台耗时方法在此执行，子线程
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            url = (String) params[1];

            imageView.setTag(url);

            return downloadBitmap(url);
        }


        //更新进度，主线程
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        //耗时方法执行结束后，主线程
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null){
                String bindUrl = (String) imageView.getTag();
                if(url.equals(bindUrl)){
                    imageView.setImageBitmap(result);

                    localCacheUtils.setBitmapToLocal(url, result);//将图片保存到本地
                    memoryCacheUtils.setBitmapToMemory(url, result);//将图片保存到内存
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url){
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                InputStream inputStream = connection.getInputStream();

                //图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;//宽高都压缩为原来的 2分之1
                options.inPreferredConfig = Bitmap.Config.RGB_565;//设置图片格式

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return null;
    }
}
