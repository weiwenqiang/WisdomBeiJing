package com.wwq.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 魏文强 on 2016/10/17.
 */
public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 表示事件是否拦截, 返回false表示不拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    /**
     * 重写onTouchEvent方法，拦截滑动事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
