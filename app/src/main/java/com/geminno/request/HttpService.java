package com.geminno.request;

import com.geminno.response.HttpResult;
import com.geminno.response.Version;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by xu on 2017/7/18.
 */

public interface HttpService {
    @GET("getversion")
    Observable<HttpResult<Version>> getCode();

    @POST("bind_data")
    @FormUrlEncoded
    Observable<HttpResult<HttpResult<String>>> bind_data(
            @Field("cph") String cph,
            @Field("sjhm") String phone
    );

    @POST("delete_data")
    @FormUrlEncoded
    Observable<HttpResult<HttpResult<String>>> delete_data(
            @Field("cph") String cph,
            @Field("sjhm") String phone
    );

}
