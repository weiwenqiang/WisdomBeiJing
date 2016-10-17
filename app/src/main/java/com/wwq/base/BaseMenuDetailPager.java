package com.wwq.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 * Created by 魏文强 on 2016/10/17.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;//根布局对象

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initViews();
    }

    /**
     * 初始化界面
     */
    public abstract View initViews();

    /**
     * 初始化数据
     */
    public void initData() {

    }
}
