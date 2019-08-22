package com.u9porn.data.network.apiservice;

import com.u9porn.data.network.Api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * @author flymegoc
 * @date 2018/1/17
 */

public interface MeiZiTuServiceApi {

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("page/{page}/")
    Observable<String> meiZiTuIndex(@Path("page") int page);

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("hot/page/{page}/")
    Observable<String> meiZiTuHot(@Path("page") int page);

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("best/page/{page}/")
    Observable<String> meiZiTuBest(@Path("page") int page);

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("xinggan/page/{page}/")
    Observable<String> meiZiTuSexy(@Path("page") int page);

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("japan/page/{page}/")
    Observable<String> meiZiTuJapan(@Path("page") int page);

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("taiwan/page/{page}/")
    Observable<String> meiZiTuJaiwan(@Path("page") int page);

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("mm/page/{page}/")
    Observable<String> meiZiTuMm(@Path("page") int page);

    @Headers({"Referer: " + Api.APP_MEIZITU_DOMAIN,
            "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
    @GET("{id}")
    Observable<String> meiZiTuImageList(@Path("id") int id);
}
