package com.geminno.fragment;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.geminno.iccardmanagerapplication.Car;
import com.geminno.iccardmanagerapplication.R;
import com.geminno.utils.Dao;
import com.geminno.utils.MyUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by xu on 2017/7/7.
 */

public class DeliveryFragment extends BaseFragment {
    private PullToRefreshWebView wb_fahuo;
    private EditText editText;
    private Button button;
    String url;
    String selectItem;
    Dao dao;
    List<Car> cheList = new ArrayList<>();
    ArrayAdapter<String> aa;
    private WebView webView;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //  auto_complete.setText("");
        } else {
            cheList = dao.getChe();
            if (cheList != null && cheList.size() > 0) {
                String firstItem = cheList.get(0).getCph();
                editText.setText(firstItem);
            } else {
                editText.setText("");
            }
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.delivery_fragment, null);
        wb_fahuo = (PullToRefreshWebView) view.findViewById(R.id.wb_fahuo);
        editText = (EditText) view.findViewById(R.id.auto_input);
        button = (Button) view.findViewById(R.id.btn_query);
        dao = new Dao(context);
        webView = wb_fahuo.getRefreshableView();
        return view;
    }

    @Override
    public void initEvent() {
     /*   editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem=adapterView.getItemAtPosition(i).toString();
            }
        });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem = editText.getText().toString();
                url = MyUtils.ip + "iccard/index.php/Home/Index/search_fh" + "?keywords=" + selectItem;
                webView.loadUrl(url);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);

            }
        });
        wb_fahuo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String label = simpleDateFormat.format(System.currentTimeMillis());
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                selectItem = editText.getText().toString();
                url = MyUtils.ip + "iccard/index.php/Home/Index/search_fh" + "?keywords=" + selectItem;
                webView.loadUrl(url);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                wb_fahuo.onRefreshComplete();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        cheList = dao.getChe();
        if (cheList != null && cheList.size() > 0) {
            String firstItem = cheList.get(0).getCph();
            editText.setText(firstItem);
        }
    }
}
