package com.geminno.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * Created by xu on 2017/7/18.
 */

public class ProgressDialogHandler extends Handler {
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    private ProgressDialog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;
    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable){
        this.cancelable=cancelable;
        this.context=context;
        this.mProgressCancelListener=mProgressCancelListener;
    }
    private void initProgressDialog(String msg){
        if(pd==null){
            pd=new ProgressDialog(context);
            pd.setMessage(msg);
            pd.setCancelable(cancelable);
            if(cancelable){
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            if(!pd.isShowing()){
                pd.show();
            }
        }

    }
    private void dismissProgressDialog(){
        if(pd!=null){
            pd.dismiss();
            pd=null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog(msg.getData().getString("msg",""));
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
