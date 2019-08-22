package com.u9porn.data.network.apiservice;

import com.u9porn.data.network.Api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * @author flymegoc
 * @date 2018/1/20
 */

public interface ProxyServiceApi {

    @Headers({"Domain-Name: " + Api.XICI_DAILI_DOMAIN_NAME})
    @GET("nn/{page}")
    Observable<String> proxyXiciDaili(@Path("page") int page);
}
