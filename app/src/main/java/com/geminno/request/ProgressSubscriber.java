package com.geminno.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by xu on 2017/7/18.
 */

public class ProgressSubscriber<T>extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;
    private boolean is_show;
    private String txt_msg;
    /**
     * 不传参数默认不显示Dialog
     *
     *
     */
 public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,Context context){
     this.context=context;
     this.mSubscriberOnNextListener=mSubscriberOnNextListener;
     this.is_show=false;
     mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
 }
    /**
     * 传参数默认显示Dialog
     *
     *
     */
 public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener,Context context,String txt_msg){
     this.context=context;
     this.mSubscriberOnNextListener=mSubscriberOnNextListener;
     this.is_show=true;
     this.txt_msg=txt_msg;
     mProgressDialogHandler=new ProgressDialogHandler(context,this,true);
 }
   public void showProgressDialog(){
       if(mProgressDialogHandler!=null){
           Message message= mProgressDialogHandler.obtainMessage();
           message.what= ProgressDialogHandler.SHOW_PROGRESS_DIALOG;
           Bundle bundle=new Bundle();
           bundle.putString("msg",txt_msg);
           message.setData(bundle);
           message.sendToTarget();
       }
   }
    private void dismissProgressDialog(){
        if(mProgressDialogHandler!=null){
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler=null;
        }
    }
    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
           if(!this.isUnsubscribed()){
               this.unsubscribe();
           }
    }

    @Override
    public void onCompleted() {
             if(is_show){
                 dismissProgressDialog();
             }
    }
    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if(e instanceof SocketTimeoutException){
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        }else if(e instanceof ConnectException){
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.e("error",e.toString());
        dismissProgressDialog();

    }
    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
     if(mSubscriberOnNextListener!=null){
         mSubscriberOnNextListener.onNext(t);
     }
    }
    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        if(is_show){
            showProgressDialog();
        }
    }
}
