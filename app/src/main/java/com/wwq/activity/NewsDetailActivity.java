package com.wwq.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.wwq.wisdombeijing.R;

/**
 * Created by 魏文强 on 2016/10/23.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {
    private WebView mWebView;
    private ImageButton btnBack;
    private ImageButton btnSize;
    private ImageButton btnShare;

    private ProgressBar pbProgress;

    private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
    private int mCurrentItem = 2;// 记录当前选中的item, 点击确定后

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        mWebView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnSize = (ImageButton) findViewById(R.id.btn_size);
        btnShare = (ImageButton) findViewById(R.id.btn_share);

        btnBack.setOnClickListener(this);
        btnSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);

        String url = getIntent().getStringExtra("url");

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//表示支持JS
        settings.setBuiltInZoomControls(true);//显示放大缩小按钮
        settings.setUseWideViewPort(true);//可以双击缩放

        mWebView.setWebViewClient(new WebViewClient(){
            //网页开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }
            //网页结束加载
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // mWebView.goBack()
        mWebView.setWebChromeClient(new WebChromeClient(){
            //网页加载进度发生改变
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.loadUrl(url.replace("10.0.2.2","192.168.0.104"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;

            default:
                break;
        }
    }

    /**
     * 显示选择对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
                "超小号字体" };
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("选中:" + which);
                        mCurrentChooseItem = which;
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWebView.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }
                mCurrentItem = mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 分享, 注意在sdcard根目录放test.jpg
     */
    private void showShare() {

    }
}
