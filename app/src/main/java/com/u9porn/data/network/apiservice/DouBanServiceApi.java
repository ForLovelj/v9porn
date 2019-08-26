package com.u9porn.data.network.apiservice;

import com.u9porn.data.network.Api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface DouBanServiceApi {
    @Headers({"Domain-Name: " + Api.DOU_BAN_DOMAIN_NAME})
    @GET("index.htm")
    Observable<String> listDouBanMeiZhi(@Query("cid") int cid, @Query("pager_offset") int page);
}
