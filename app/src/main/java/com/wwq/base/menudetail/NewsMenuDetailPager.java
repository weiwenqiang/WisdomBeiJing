package com.wwq.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wwq.base.BaseMenuDetailPager;
import com.wwq.base.TabDetailPager;
import com.wwq.domain.NewsData;
import com.wwq.wisdombeijing.R;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ViewPager viewPager;
    private ArrayList<TabDetailPager> pagers;
    private ArrayList<NewsData.NewsTabData> newsTabDatas;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        newsTabDatas = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);

        viewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);


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
    }

    class MenuDetailAdapter extends PagerAdapter{

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
