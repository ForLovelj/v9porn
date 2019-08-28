package com.u9porn.data.network.apiservice;

import com.u9porn.data.network.Api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface KeDouServiceApi {

    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET("{category}/{page}/")
    Observable<String> videoList(@Path("category") String category,@Path("page") int page);

    /**
     * 最新的
     * @param page
     * @return
     */
    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET("latest-updates/{page}/")
    Observable<String> videoListLatest(@Path("page") int page);

    /**
     *最受欢迎的
     * @param page
     * @return
     */
    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET("top-rated/{page}/")
    Observable<String> videoListTop(@Path("page") int page);

    /**
     * 流行的
     * @param page
     * @return
     */
    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET("most-popular/{page}/")
    Observable<String> videoListPopular(@Path("page") int page);

    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET
    Observable<String> videoDetail(@Url String url);

    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET
    Observable<String> getRealVideoUrl(@Url String url);
}
