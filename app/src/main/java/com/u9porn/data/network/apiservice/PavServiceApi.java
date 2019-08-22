package com.u9porn.data.network.apiservice;

import com.u9porn.data.network.Api;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public interface PavServiceApi {

    /**
     * 视频列表
     *
     * @param url 链接
     * @return ob
     */
    @Headers({"Domain-Name: " + Api.PA_DOMAIN_NAME})
    @GET
    Observable<String> pigAvVideoList(@Url String url);

    /**
     * 视频链接
     *
     * @param url 链接
     * @return ob
     */
    @Headers({"Domain-Name: " + Api.PA_DOMAIN_NAME})
    @GET
    Observable<String> pigAvVideoUrl(@Url String url);

    /**
     * 更多数据，严格说其实根本不分类，所有分类都一样,反正参数里看不出来，也行存cookie了
     *
     * @return ob
     */
    @Headers({"Domain-Name: " + Api.PA_DOMAIN_NAME})
    @FormUrlEncoded
    @POST("wp-admin/admin-ajax.php")
    Observable<String> moreVideoList(@FieldMap Map<String, String> fileMap);
}
