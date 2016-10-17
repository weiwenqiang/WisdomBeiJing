package com.wwq.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wwq.base.BaseMenuDetailPager;
import com.wwq.base.BasePager;
import com.wwq.base.menudetail.InteractMenuDetailPager;
import com.wwq.base.menudetail.NewsMenuDetailPager;
import com.wwq.base.menudetail.PhotoMenuDetailPager;
import com.wwq.base.menudetail.TopicMenuDetailPager;
import com.wwq.domain.NewsData;
import com.wwq.fragment.LeftMenuFragment;
import com.wwq.global.GlobalContants;
import com.wwq.wisdombeijing.MainActivity;

import java.util.ArrayList;

/**
 * 新闻中心
 */
public class NewsCenterPager extends BasePager {

    private ArrayList<BaseMenuDetailPager> mPagers;// 4个菜单详情页的集合
    private NewsData mNewsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("初始化新闻中心数据....");

        tvTitle.setText("新闻");
        setSlidingMenuEnable(true);// 打开侧边栏

        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();

        // 使用xutils发送请求
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.CATEGORIES_URL,
                new RequestCallBack<String>() {

                    // 访问成功
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String result = (String) responseInfo.result;
                        System.out.println("返回结果:" + result);

                        parseData(result);
                    }

                    // 访问失败
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                                .show();
                        error.printStackTrace();
                    }

                });
    }

    //解析网络数据
    protected void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
        System.out.println("解析结果:" + mNewsData);

        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();

        leftMenuFragment.setMenuData(mNewsData);

        // 准备4个菜单详情页
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuDetailPager(0);//设置默认详情页
    }

    //设置菜单详情页
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = mPagers.get(position);
        flContent.removeAllViews();
        flContent.addView(pager.mRootView);
        //设置当前页的标题
        NewsData.NewsMenuData newsMenuData = mNewsData.data.get(position);
        tvTitle.setText(newsMenuData.title);

        pager.initData();//初始化当前也数据
    }
}
