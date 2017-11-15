package com.geminno.fragment;


import android.app.DatePickerDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.geminno.iccardmanagerapplication.Car;
import com.geminno.iccardmanagerapplication.R;
import com.geminno.utils.Dao;
import com.geminno.utils.MyUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by xu on 2017/7/19.
 */

public class StatusFragment extends BaseFragment implements View.OnTouchListener {
    private EditText startTime;
    private EditText endTime;
    private Button btn_query;
    private EditText editText;
    Dao dao;
    List<Car> cheList = new ArrayList<Car>();
    ArrayAdapter<String> aa;
    String b;
    private PullToRefreshWebView query_status;
    private WebView webView;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

            cheList = dao.getChe();
            if (cheList != null && cheList.size() > 0) {
                String firstItem = cheList.get(0).getCph();
                editText.setText(firstItem);
            } else {
                editText.setText("");
            }
          /*  long current=System.currentTimeMillis();//当前时间毫秒数
            long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
            long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
            startTime.setText(new Timestamp(zero)+"");
            endTime.setText(new Timestamp(twelve)+"");*/
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date start = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.SECOND, -1);

            Date end = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startTime.setText(sdf.format(start));
            endTime.setText(sdf.format(end));
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.status_fragment, null);
        startTime = (EditText) view.findViewById(R.id.start_pick);
        endTime = (EditText) view.findViewById(R.id.end_pick);
        btn_query = (Button) view.findViewById(R.id.btn_query);
        editText = (EditText) view.findViewById(R.id.edit_input);
        query_status = (PullToRefreshWebView) view.findViewById(R.id.query_status);
        dao = new Dao(context);
        webView = query_status.getRefreshableView();
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        cheList = dao.getChe();

        if (cheList != null && cheList.size() > 0) {
            String firstItem = cheList.get(0).getCph();
            editText.setText(firstItem);
        }
      /*  aa=new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,cheList);
        editText.setAdapter(aa);*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date start = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        Date end = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startTime.setText(sdf.format(start));
        endTime.setText(sdf.format(end));
    }

    @Override
    public void initEvent() {
        super.initEvent();
        startTime.setOnTouchListener(this);
        endTime.setOnTouchListener(this);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = MyUtils.ip + "iccard/index.php/Home/Index/search_wl" + "?cph=" + editText.getText().toString() + "&" + "start_time=" + startTime.getText().toString() + "&" + "end_time=" + endTime.getText().toString();
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
        query_status.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String label = simpleDateFormat.format(System.currentTimeMillis());
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                String url = MyUtils.ip + "iccard/index.php/Home/Index/search_wl" + "?cph=" + editText.getText().toString() + "&" + "start_time=" + startTime.getText().toString() + "&" + "end_time=" + endTime.getText().toString();

                Log.i("DeliveryFragment", url);
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
                query_status.onRefreshComplete();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (v.getId() == R.id.start_pick) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (i1 < 9 && i2 < 10) {
                            startTime.setText(i + "-" + "0" + (1 + i1) + "-" + "0" + i2);
                        } else if (i1 >= 9 && i2 < 10) {
                            startTime.setText(i + "-" + (1 + i1) + "-" + "0" + i2);
                        } else if (i1 < 9 && i2 >= 10) {
                            startTime.setText(i + "-" + "0" + (1 + i1) + "-" + i2);
                        } else if (i >= 9 && i2 >= 10) {
                            startTime.setText(i + "-" + (1 + i1) + "-" + i2);
                        }
                    }
                }
                        , calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH)).show();

            } else if (v.getId() == R.id.end_pick) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (i1 < 9 && i2 < 10) {
                            endTime.setText(i + "-" + "0" + (1 + i1) + "-" + "0" + i2);
                        } else if (i1 >= 9 && i2 < 10) {
                            endTime.setText(i + "-" + (1 + i1) + "-" + "0" + i2);
                        } else if (i1 < 9 && i2 >= 10) {
                            endTime.setText(i + "-" + "0" + (1 + i1) + "-" + i2);
                        } else if (i >= 9 && i2 >= 10) {
                            endTime.setText(i + "-" + (1 + i1) + "-" + i2);
                        }
                    }
                }
                        , calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
          /*  AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = View.inflate(context, R.layout.date_time_dialog, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            if (v.getId() == R.id.start_pick) {
                final int inType = startTime.getInputType();
                startTime.setInputType(InputType.TYPE_NULL);
                startTime.onTouchEvent(motionEvent);
                startTime.setInputType(inType);
                startTime.setSelection(startTime.getText().length());

                builder.setTitle("选取起始时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb.append(" ");
                        sb.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute());
                        startTime.setText(sb);

                        startTime.requestFocus();
                        dialog.cancel();

                    }
                });

            } else if (v.getId() == R.id.end_pick) {
                int inType = endTime.getInputType();
                endTime.setInputType(InputType.TYPE_NULL);
                endTime.onTouchEvent(motionEvent);
                endTime.setInputType(inType);
                endTime.setSelection(endTime.getText().length());

                builder.setTitle("选取结束时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb1 = new StringBuffer();
                        sb1.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb1.append(" ");
                        sb1.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute());
                        endTime.setText(sb1);

                        dialog.cancel();
                    }
                });
            }
            Dialog dialog = builder.create();
            dialog.show();*/

        }

        return true;
    }
}
