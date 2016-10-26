package com.wwq.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wwq.base.BaseMenuDetailPager;
import com.wwq.domain.PhotosData;
import com.wwq.global.GlobalContants;
import com.wwq.utils.CacheUtils;
import com.wwq.utils.bitmap.MyBitmapUtils;
import com.wwq.wisdombeijing.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * 菜单详情页-组图
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	private ListView lvPhoto;
	private GridView gvPhoto;
	private ArrayList<PhotosData.PhotoInfo> mPhotoList;
	private PhotoAdapter mAdapter;
	private ImageButton btnPhoto;
	private boolean isListDisplay = true;// 是否是列表展示

	public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		this.btnPhoto = btnPhoto;
		btnPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeDisplay();
			}
		});
	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);

		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);

		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(mActivity, GlobalContants.PHOTOS_URL);
		if(!TextUtils.isEmpty(cache)){

		}
		getDataFromServer();
	}

	private void getDataFromServer(){
		HttpUtils utils = new HttpUtils();
		utils.send(HttpRequest.HttpMethod.GET, GlobalContants.PHOTOS_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				parseData(result);
				// 设置缓存
				CacheUtils.setCache(mActivity, GlobalContants.PHOTOS_URL, result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	protected void parseData(String result){
		Gson gson = new Gson();
		PhotosData data = gson.fromJson(result, PhotosData.class);

		mPhotoList = data.data.news;// 获取组图列表集合

		if (mPhotoList != null) {
			mAdapter = new PhotoAdapter();
			lvPhoto.setAdapter(mAdapter);
			gvPhoto.setAdapter(mAdapter);
		}
	}


	class PhotoAdapter extends BaseAdapter {

		private final MyBitmapUtils utils;

		public PhotoAdapter() {
			utils = new MyBitmapUtils();
//			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mPhotoList.size();
		}

		@Override
		public PhotosData.PhotoInfo getItem(int position) {
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_photo_item,
						null);

				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PhotosData.PhotoInfo item = getItem(position);

			holder.tvTitle.setText(item.title);

			String imgUrl = item.listimage;
			utils.display(holder.ivPic, imgUrl.replace("10.0.2.2","192.168.0.104"));
			return convertView;
		}
	}
	static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivPic;
	}

	/**
	 * 切换展现方式
	 */
	private void changeDisplay() {
		if (isListDisplay) {
			isListDisplay = false;
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);

			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);

		} else {
			isListDisplay = true;
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);

			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}
}
