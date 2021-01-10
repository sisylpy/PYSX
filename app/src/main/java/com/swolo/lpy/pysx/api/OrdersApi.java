package com.swolo.lpy.pysx.api;


import com.swolo.lpy.pysx.http.CommonResponse;
import com.swolo.lpy.pysx.main.modal.Orders;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public interface OrdersApi {




    @GET("api/nxrestrauntorders/weighingGetOrders/{goodsId}")
    Observable<CommonResponse> weighingGetOrders(@Path("goodsId") String goodsId);


    @FormUrlEncoded
    @POST("api/nxrestrauntorders/printOrder")
    Observable<CommonResponse> printOrder(@Field("orderId") Integer orderId,
                                          @Field("orderWeight") String orderWeight);






}
