package com.wwq.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wwq.utils.DensityUtils;
import com.wwq.utils.SPUtils;
import com.wwq.wisdombeijing.MainActivity;
import com.wwq.wisdombeijing.R;

import java.util.ArrayList;

/**
 * Created by 魏文强 on 2016/10/12.
 */
public class GuideActivity extends Activity {
    private ViewPager viewPager;
    private LinearLayout ll_point_group;//引导圆点的父控件
    private View viewRedPoint;
    private Button bt_start;

    private ArrayList<ImageView> imageViewArrayList;

    private static final int[] mImageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    private int mPointWidth;//圆点间的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager) findViewById(R.id.vp_guide);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        viewRedPoint = findViewById(R.id.view_red_point);
        bt_start = (Button) findViewById(R.id.bt_start);
        initView();
        viewPager.setAdapter(new GuideAdapter());
        viewPager.setOnPageChangeListener(new GuidePageListener());
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp,表示已经展示了新手引导
                SPUtils.put(GuideActivity.this,"is_user_guide_showed",true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initView() {
        imageViewArrayList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            imageViewArrayList.add(imageView);
        }

        //初始化引导页圆点
        for (int i = 0; i < mImageIds.length; i++) {
            View point = new ImageView(this);
            int px = DensityUtils.dp2px(this, 10);//屏幕适配
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
            if (i > 0) {
                params.leftMargin = 20;//圆点间隔
            }
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.shape_point_guide);
            ll_point_group.addView(point);
        }


        //获取视图树
        ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {//当Layout执行结束后回调此方法
                System.out.println("layout 结束");
                mPointWidth = ll_point_group.getChildAt(1).getLeft()
                        - ll_point_group.getChildAt(0).getLeft();

                ll_point_group.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewArrayList.get(position));
            return imageViewArrayList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class GuidePageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //滑动事件
            System.out.println("当前位置：" + position + "; 百分比：" + positionOffset + "; 移动距离：" + positionOffsetPixels);
            float len = mPointWidth * positionOffset + position * mPointWidth;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();//获得红点的布局参数
            layoutParams.leftMargin = (int) len;
            viewRedPoint.setLayoutParams(layoutParams);
        }

        @Override
        public void onPageSelected(int position) {
            if (position == mImageIds.length - 1) {
                bt_start.setVisibility(View.VISIBLE);
            } else {
                bt_start.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
