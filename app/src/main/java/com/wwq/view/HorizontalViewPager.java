package com.wwq.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 魏文强 on 2016/10/19.
 */
public class HorizontalViewPager extends ViewPager {
    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件分发
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentItem() != 0){//如果是第一个页面，需要显示侧边栏
            getParent().requestDisallowInterceptTouchEvent(true);
        }else{
            getParent().requestDisallowInterceptTouchEvent(false);//拦截
        }
        return super.dispatchTouchEvent(ev);
    }
}
