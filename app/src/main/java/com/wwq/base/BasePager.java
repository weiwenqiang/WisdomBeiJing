package com.wwq.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wwq.wisdombeijing.MainActivity;
import com.wwq.wisdombeijing.R;

/**
 * 主页下5个子页面基类
 * Created by 魏文强 on 2016/10/16.
 */
public class BasePager {
    public Activity mActivity;
    public View mRootView;

    public TextView tvTitle;// 标题对象

    public FrameLayout flContent;// 内容

    public ImageButton btnMenu;// 菜单按钮

    public ImageButton btnPhoto;//组图切换按钮

    public BasePager(Activity activity){
        mActivity = activity;
        initViews();
    }

    public void initViews(){
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);

        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     */
    protected void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();// 切换状态, 显示时隐藏, 隐藏时显示
    }

    public void initData(){

    }

    /**
     * 设置侧边栏开启或关闭
     */
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi = (MainActivity) mActivity;

        SlidingMenu slidingMenu = mainUi.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

}
