package com.wwq.fragment;

import android.view.View;
import android.widget.RadioGroup;

import com.wwq.wisdombeijing.R;

/**
 * Created by 魏文强 on 2016/10/15.
 */
public class ContentFragment extends BaseFragment {
    private RadioGroup radioGroup;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        radioGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        radioGroup.check(R.id.rb_home);//默认勾选首页
        super.initData();

    }
}