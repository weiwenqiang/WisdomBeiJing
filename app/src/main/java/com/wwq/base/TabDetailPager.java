package com.wwq.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wwq.domain.NewsData;

/**
 * 页签详情页
 * Created by 魏文强 on 2016/10/17.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private NewsData.NewsTabData data;

    private TextView text;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData data) {
        super(mActivity);
        this.data = data;
    }

    @Override
    public View initViews() {
        text = new TextView(mActivity);
        text.setText("页签详情页");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        return text;
    }

    @Override
    public void initData() {
        text.setText(data.title);
    }
}
