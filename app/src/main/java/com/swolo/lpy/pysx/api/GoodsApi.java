package com.swolo.lpy.pysx.api;


import android.content.Intent;

import com.swolo.lpy.pysx.http.CommonResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public interface GoodsApi {



    @FormUrlEncoded
    @POST("api/nxrestrauntorders/weighingGetOrderGoodsType")
    Observable<CommonResponse> weighingGetOrderGoodsType(@Field("comId") String comId,
                                              @Field("type") String type);

    @FormUrlEncoded
    @POST("api/nxrestrauntorders/weighingGetGoods")
    Observable<CommonResponse> weighingGetGoods(@Field("fatherId") String fatherId,
                                                @Field("type")String type);






}
