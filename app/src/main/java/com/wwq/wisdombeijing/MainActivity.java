package com.wwq.wisdombeijing;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wwq.fragment.ContentFragment;
import com.wwq.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {
    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_CONTENT = "fragment_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setBehindContentView(R.layout.left_menu);// 设置侧边栏布局
//        SlidingMenu slidingMenu = getSlidingMenu();// 获取侧边栏对象
//        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
//        slidingMenu.setSecondaryMenu(R.layout.right_menu);// 设置右侧边栏
//        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 设置展现模式


        setBehindContentView(R.layout.activity_left_menu);// 设置侧边栏
        SlidingMenu slidingMenu = getSlidingMenu();// 获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
        slidingMenu.setBehindOffset(200);// 设置预留屏幕的宽度

        initFragment();
    }

    /**
     * 初始化Fragment,填充布局文件
     */
    private void initFragment(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();//开启事务
        ft.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAGMENT_LEFT_MENU);
        ft.replace(R.id.fl_content, new ContentFragment(), FRAGMENT_CONTENT);
        ft.commit();//提交事务

        // Fragment leftMenuFragment = fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
    }

    // 获取侧边栏fragment
    public LeftMenuFragment getLeftMenuFragment(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return leftMenuFragment;
    }

    // 获取主页面fragment
    public ContentFragment getContentFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }
}
