package com.geminno.fragment;

import android.util.Log;
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

public class QueueFragment extends  BaseFragment {

    Dao dao;
    EditText editText;
    List<Car> cheList=new ArrayList<>();
    ArrayAdapter<String> aa;
  PullToRefreshWebView query_chepai;
    private Button btn_query;
    String selectItem;

    private WebView webView;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
          //  auto_complete.setText("");
        }else{
            cheList=dao.getChe();
          /*  aa=new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,cheList);
            auto_complete.setAdapter(aa);*/

            if(cheList!=null&&cheList.size()>0) {
                String firstItem = cheList.get(0).getCph();
                editText.setText(firstItem);
            }else{
                editText.setText("");
            }
        }
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.queue_fragment,null);
       editText=(EditText) view.findViewById(R.id.auto_complete);
        query_chepai=(PullToRefreshWebView) view.findViewById(R.id.query_chepai);
        btn_query=(Button)view.findViewById(R.id.btn_query);
        webView=query_chepai.getRefreshableView();
        dao=new Dao(context);
        return view;
    }

    @Override
    public void initEvent() {
        super.initEvent();
      /*  auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem=adapterView.getItemAtPosition(i).toString();
                Log.i("DeliveryFragment",selectItem+"选");
            }
        });*/
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem=editText.getText().toString();
                Log.i("DeliveryFragment",selectItem+"写");
              String url= MyUtils.ip+"iccard/Home/index/search_pd.html"+"?keywords="+selectItem;
                Log.i("DeliveryFragment",url);
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
        });
        query_chepai.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String label = simpleDateFormat.format(System.currentTimeMillis());
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                selectItem=editText.getText().toString();
                String url=MyUtils.ip+"iccard/Home/index/search_pd.html"+"?keywords="+selectItem;
                Log.i("DeliveryFragment",url);
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
                query_chepai.onRefreshComplete();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        cheList=dao.getChe();
        if(cheList!=null&&cheList.size()>0) {
            String firstItem = cheList.get(0).getCph();
            editText.setText(firstItem);
        }
    }

}
