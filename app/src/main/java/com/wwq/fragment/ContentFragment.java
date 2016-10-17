package com.wwq.fragment;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wwq.base.BasePager;
import com.wwq.base.impl.GovAffairsPager;
import com.wwq.base.impl.HomePager;
import com.wwq.base.impl.NewsCenterPager;
import com.wwq.base.impl.SettingPager;
import com.wwq.base.impl.SmartServicePager;
import com.wwq.wisdombeijing.R;

import java.util.ArrayList;

/**
 * Created by 魏文强 on 2016/10/15.
 */
public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.rg_group)
    private RadioGroup radioGroup;
    @ViewInject(R.id.vp_content)
    private ViewPager viewPager;

    private ArrayList<BasePager> mPagerList;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view); //注入view和事件
        return view;
    }

    @Override
    public void initData() {
        radioGroup.check(R.id.rb_home);//默认勾选首页
        mPagerList = new ArrayList<BasePager>();
//        for (int i=0; i<5;i++){
//            BasePager basePager = new BasePager(mActivity);
//            mPagerList.add(basePager);
//        }
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));

        viewPager.setAdapter(new ContentAdapter());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        // mViewPager.setCurrentItem(0);// 设置当前页面
                        viewPager.setCurrentItem(0, false);// 去掉切换页面的动画
                        break;
                    case R.id.rb_news:
                        viewPager.setCurrentItem(1, false);// 设置当前页面
                        break;
                    case R.id.rb_smart:
                        viewPager.setCurrentItem(2, false);// 设置当前页面
                        break;
                    case R.id.rb_gov:
                        viewPager.setCurrentItem(3, false);// 设置当前页面
                        break;
                    case R.id.rb_setting:
                        viewPager.setCurrentItem(4, false);// 设置当前页面
                        break;
                    default:
                        break;
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();//获取当前被选中的页面，初始化当前页的数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerList.get(0).initData();// 初始化首页数据
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
//            pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPagerList.get(1);
    }

}