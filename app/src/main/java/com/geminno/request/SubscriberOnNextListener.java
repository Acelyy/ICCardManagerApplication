package com.geminno.request;

/**
 * Created by xu on 2017/7/18.
 */

public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
