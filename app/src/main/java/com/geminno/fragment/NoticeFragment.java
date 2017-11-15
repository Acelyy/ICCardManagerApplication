package com.geminno.fragment;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.geminno.iccardmanagerapplication.R;
import com.geminno.utils.MyUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import java.text.SimpleDateFormat;

/**
 * Created by xu on 2017/7/7.
 */

public class NoticeFragment extends BaseFragment {
   private PullToRefreshWebView notice_web;
   private WebView webView;
    String url;
    private ImageView img_back;
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.notice_fragment, null);
        notice_web=(PullToRefreshWebView)view.findViewById(R.id.notice_web);
        img_back=(ImageView)view.findViewById(R.id.img_back);
        webView=notice_web.getRefreshableView();
        return view;
    }

    @Override
    public void initData() {
       url= MyUtils.ip+"iccard/index.php/Home/Index/notice_list";
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
  
    }

    @Override
    public void initEvent() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onKeyDown();
            }
        });
    }

    public boolean onKeyDown(){
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
           getActivity().finish();
            return false;
        }
    }
}
