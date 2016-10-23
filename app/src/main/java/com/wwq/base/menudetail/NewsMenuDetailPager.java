package com.wwq.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.wwq.base.BaseMenuDetailPager;
import com.wwq.base.TabDetailPager;
import com.wwq.domain.NewsData;
import com.wwq.wisdombeijing.MainActivity;
import com.wwq.wisdombeijing.R;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private ArrayList<TabDetailPager> pagers;
    private ArrayList<NewsData.NewsTabData> newsTabDatas;
    private TabPageIndicator indicator;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        newsTabDatas = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);

        ViewUtils.inject(this, view);

        viewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
        indicator = (TabPageIndicator)view.findViewById(R.id.indicator);

//        viewPager.setOnPageChangeListener(this);//当ViewPager和Indicator绑定时，应该把滑动监听设置给Indicator才有效
        indicator.setOnPageChangeListener(this);

        return view;
    }

    @Override
    public void initData() {
        pagers = new ArrayList<TabDetailPager>();
        for(int i = 0; i < newsTabDatas.size(); i++){
            TabDetailPager pager = new TabDetailPager(mActivity, newsTabDatas.get(i));
            pagers.add(pager);
        }
        viewPager.setAdapter(new MenuDetailAdapter());
        indicator.setViewPager(viewPager);//必须在viewPager设置完adapter之后才能调用
    }

    @OnClick(R.id.btn_next)
    public void nextPage(View view){
        int currentItem = viewPager.getCurrentItem();
        viewPager.setCurrentItem(++currentItem);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if(position == 0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MenuDetailAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            return newsTabDatas.get(position).title;
        }

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = pagers.get(position);
            container.addView(tabDetailPager.mRootView);
            tabDetailPager.initData();
            return tabDetailPager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
