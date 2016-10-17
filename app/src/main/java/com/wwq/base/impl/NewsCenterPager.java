package com.wwq.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wwq.base.BasePager;

/**
 * 新闻中心
 */
public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("初始化首页数据....");

        tvTitle.setText("新闻中心");// 修改标题
        btnMenu.setVisibility(View.GONE);// 隐藏菜单按钮
        setSlidingMenuEnable(false);//关闭侧边栏

        TextView text = new TextView(mActivity);
        text.setText("新闻中心");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态添加布局
        flContent.addView(text);
    }
}
