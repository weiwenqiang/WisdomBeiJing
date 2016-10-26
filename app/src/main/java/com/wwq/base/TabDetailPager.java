package com.wwq.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.wwq.activity.NewsDetailActivity;
import com.wwq.domain.NewsData;
import com.wwq.domain.TabData;
import com.wwq.global.GlobalContants;
import com.wwq.utils.CacheUtils;
import com.wwq.utils.SPUtils;
import com.wwq.utils.bitmap.MyBitmapUtils;
import com.wwq.view.RefreshListView;
import com.wwq.wisdombeijing.R;

import java.util.ArrayList;

/**
 * 页签详情页
 * Created by 魏文强 on 2016/10/17.
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    private NewsData.NewsTabData data;

    @ViewInject(R.id.tv_title)
    private TextView tvText;

    private String mUrl;

    @ViewInject(R.id.vp_news)
    private ViewPager viewPager;

    private TabData mTabDetailData;
    private ArrayList<TabData.TopNewsData> topNewsDatas;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;
    @ViewInject(R.id.lv_list)
    private RefreshListView listView;
    private ArrayList<TabData.TabNewsData> newsDatas;
    private String mMoreUrl;
    private NewsAdapter newsAdapter;

    private Handler mHandler;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData data) {
        super(mActivity);
        this.data = data;
        mUrl = GlobalContants.SERVER_URL + data.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);

        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        listView.addHeaderView(headerView);

        listView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT)
                            .show();
                    listView.onRefreshComplete(false);// 收起加载更多的布局
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("被点击" + position);

                // 在本地记录已读状态
                String ids = (String) SPUtils.get(mActivity, "read_ids", "");
                String readId = newsDatas.get(position).id;
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    SPUtils.put(mActivity, "read_ids", ids);
                }

                // mNewsAdapter.notifyDataSetChanged();
                changeReadState(view);// 实现局部界面刷新, 这个view就是被点击的item布局对象

                // 跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", newsDatas.get(position).url);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if(!TextUtils.isEmpty(cache)){
            parseData(cache, false);
        }
        getDataFromService();
    }

    private void getDataFromService(){
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("页签详情页返回结果:" + result);

                parseData(result, false);

                CacheUtils.setCache(mActivity, mUrl, result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    protected void parseData(String result, boolean isMore){
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("页签详情页：" + mTabDetailData.toString());

        //处理更多页面逻辑
        String more = mTabDetailData.data.more;
        if(!TextUtils.isEmpty(more)){
            mMoreUrl = GlobalContants.SERVER_URL+ more;
        }else{
            mMoreUrl = null;
        }

        if(!isMore) {
            topNewsDatas = mTabDetailData.data.topnews;

            newsDatas = mTabDetailData.data.news;

            viewPager.setAdapter(new TopNewsAdapter());

            if (topNewsDatas != null) {
                indicator.setViewPager(viewPager);
                indicator.setSnap(true);
                indicator.setOnPageChangeListener(this);
                indicator.onPageSelected(0);//让指示器重新定位到第一个

                tvText.setText(topNewsDatas.get(0).title);
            }

            if (newsDatas != null) {
                newsAdapter = new NewsAdapter();
                listView.setAdapter(newsAdapter);
            }

            if(mHandler == null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = viewPager.getCurrentItem();
                        if(currentItem < topNewsDatas.size() -1){
                            currentItem++;
                        }else{
                            currentItem = 0;
                        }
                        viewPager.setCurrentItem(currentItem);

                        mHandler.sendEmptyMessageDelayed(0, 3000);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 3000);
            }
        }else{
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news;
            newsDatas.addAll(news);
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabData.TopNewsData topNewsData = topNewsDatas.get(position);
        tvText.setText(topNewsData.title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class TopNewsAdapter extends PagerAdapter{
//        private BitmapUtils utils;
        private MyBitmapUtils utils;
        public TopNewsAdapter(){
//            utils = new BitmapUtils(mActivity);
//            utils.configDefaultLoadingImage(R.drawable.topnews_item_default);//设置默认图片
            utils = new MyBitmapUtils();
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//基于控件大小填充图片

            TabData.TopNewsData topNewsData = topNewsDatas.get(position);
            String imgUrl = topNewsData.topimage;
            utils.display(imageView, imgUrl.replace("10.0.2.2","192.168.0.104"));//传递imageView对象和图片地址

            container.addView(imageView);

            imageView.setOnTouchListener(new TopNewsTouchListener());

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class TopNewsTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mHandler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息
//                     mHandler.postDelayed(new Runnable() {
//                         @Override
//                         public void run() {
//                         }
//                     }, 3000);
                    break;
                case MotionEvent.ACTION_UP:
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mHandler.sendEmptyMessageDelayed(0, 3000);//为了防止事件取消
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    class NewsAdapter extends BaseAdapter{

        private final BitmapUtils utils;

        public NewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return newsDatas.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return newsDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item,
                        null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView
                        .findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView
                        .findViewById(R.id.tv_date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item = getItem(position);

            holder.tvTitle.setText(item.title);
            holder.tvDate.setText(item.pubdate);

            String imgUrl = item.listimage;
            utils.display(holder.ivPic, imgUrl.replace("10.0.2.2","192.168.0.104"));

            String ids  = (String) SPUtils.get(mActivity, "read_ids", "");
            if(ids.contains(getItem(position).id)){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else{
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }
    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivPic;
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("页签详情页返回结果:" + result);

                parseData(result, false);

                listView.onRefreshComplete(true);

                // 设置缓存
//                CacheUtils.setCache(mUrl, result, mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();

                listView.onRefreshComplete(false);
            }
        });
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;

                parseData(result, true);

                listView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                listView.onRefreshComplete(false);
            }
        });
    }
    /**
     * 改变已读新闻的颜色
     */
    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }
}
