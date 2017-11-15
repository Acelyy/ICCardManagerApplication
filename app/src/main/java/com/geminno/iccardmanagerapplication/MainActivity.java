package com.geminno.iccardmanagerapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.geminno.fragment.BaseFragment;
import com.geminno.fragment.BindFragment;
import com.geminno.fragment.DeliveryFragment;
import com.geminno.fragment.NoticeFragment;
import com.geminno.fragment.QueueFragment;
import com.geminno.fragment.StatusFragment;
import com.geminno.request.HttpUtil;
import com.geminno.request.ProgressSubscriber;
import com.geminno.request.SubscriberOnNextListener;
import com.geminno.response.HttpResult;
import com.geminno.response.Version;
import com.geminno.utils.DownLoadRunnable;
import com.geminno.utils.MyUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<BaseFragment> fragments;
    private int position = 0;
    private Fragment saveFragment;//上一次显示的fragment；
    FrameLayout frameLayout;
    RadioGroup rg;

    //handler更新ui
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    install(MainActivity.this);
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_FAILED:
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        initEvent();
        getVersion();

    }



    private void downloadApk(String url) {
        new Thread(new DownLoadRunnable(MainActivity.this, url, "停车场IC管理", 0, handler)).start();
    }

    private String getLocalVersionName() {
        PackageManager pm = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = pm.getPackageInfo(getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        rg = (RadioGroup) findViewById(R.id.rg_main);
    }

    private void initEvent() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                String tag = null;
                switch (i) {
                    case R.id.rb_delivery:
                        position = 0;
                        tag = "delivery";
                        break;
                    case R.id.rb_status:
                        position = 1;
                        tag = "status";
                        break;
                    case R.id.rb_queue:
                        position = 2;
                        tag = "queue";
                        break;
                    case R.id.rb_notice:
                        position = 3;
                        tag = "notice";
                        break;
                    case R.id.rb_bind:
                        position = 4;
                        break;
                    default:
                        position = 0;
                        break;
                }
                BaseFragment baseFragment = getFragment(position);
                switchFragment(saveFragment, baseFragment, tag);
            }
        });
        //默认选中首页
        rg.check(R.id.rb_delivery);
    }

    private void switchFragment(Fragment fromFragment, BaseFragment nextFragment, String tag) {
        if (saveFragment != nextFragment) {
            saveFragment = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (!nextFragment.isAdded()) {
                    if (fromFragment != null) {
                        ft.hide(fromFragment);
                    }
                    ft.add(R.id.frameLayout, nextFragment, tag).commit();
                } else {
                    if (fromFragment != null) {
                        ft.hide(fromFragment);
                    }
                    ft.show(nextFragment).commit();
                }
            }
        }
    }

    private BaseFragment getFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            BaseFragment baseFragment = fragments.get(position);
            return baseFragment;
        }
        return null;
    }

    private void initFragment() {
        fragments = new ArrayList<BaseFragment>();
        fragments.add(new DeliveryFragment());
        fragments.add(new StatusFragment());
        fragments.add(new QueueFragment());
        fragments.add(new NoticeFragment());
        fragments.add(new BindFragment());
    }

    public void install(Context context) {
        Log.i("install", "start");
        File file = MyUtils.getCacheFile(MyUtils.APP_NAME, context);
        if (file == null || !file.exists()) {
            return;
        }
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installintent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(installintent);
        Log.i("install", "finish");
    }

    public NoticeFragment getFragmentByTag() {
        FragmentManager fm = getSupportFragmentManager();
        NoticeFragment noticeFragment = (NoticeFragment) fm.findFragmentByTag("notice");
        return noticeFragment;
    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Version>() {
            @Override
            public void onNext(Version data) {
                double version_num = data.getVersion_num();
               final String version_url = data.getVersion_url();
                if (new Double(Double.parseDouble(getLocalVersionName())).compareTo(new Double(version_num)) <0) {
                    System.out.println(getLocalVersionName());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("版本更新");
                    dialog.setMessage("有新的版本，需要更新吗");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            downloadApk(version_url);
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            }
        };
        HttpUtil.getInstance().getVersion(new ProgressSubscriber<HttpResult<Version>>(onNextListener, this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            NoticeFragment noticeFragment = getFragmentByTag();
            if (noticeFragment != null) {
                if (noticeFragment.onKeyDown()) {
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
