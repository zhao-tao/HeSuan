package com.sxonecard.http;


import com.sxonecard.http.bean.ShutDownBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liukun on 16/3/9.
 */
public interface HttpDataService {
   /*  //post数据不使用key.
    @FormUrlEncoded
    @POST("api/index.php")
     Observable<HttpResult<TradeStatusBean>> uploadTrade(@Body String param);

    //post数据使用key
    @FormUrlEncoded
    @POST("api/index.php?Act=run")
    Observable<HttpResult<AdBean>> breathTick(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("api/index.php?Act=run")
    Observable<HttpResult<String>> phphreat(@FieldMap Map<String,String> param);
    */
    /**
     * 开关机设置接口.
     * @param imeiId
     * @return
     */
    @GET("/api/index.php?Act=set")
    Observable<HttpResult<ShutDownBean>> shutDown(@Query("ImeiId") String imeiId);
}
