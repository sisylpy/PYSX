package com.swolo.lpy.pysx.main.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swolo.lpy.pysx.api.GoodsApi;
import com.swolo.lpy.pysx.api.OrdersApi;
import com.swolo.lpy.pysx.http.CommonResponse;
import com.swolo.lpy.pysx.http.HttpManager;
import com.swolo.lpy.pysx.main.modal.ComuGoods;
import com.swolo.lpy.pysx.main.modal.Orders;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class OrdersPresenterImpl implements MainContract.OrdersPresenter {

    private MainContract.OrdersView mView;

    public OrdersPresenterImpl(MainContract.OrdersView mView) {
        this.mView = mView;
    }

    @Override
    public void getOrdersList(String goodsId) {
        HttpManager.getInstance().request(HttpManager.getInstance().getApi(OrdersApi.class).weighingGetOrders(goodsId))
                .map(new Func1<String, List<Orders>>() {
                    @Override
                    public List<Orders> call(String s) {

                        List<Orders> orders = new Gson().fromJson(s, new TypeToken<List<Orders>>() {
                        }.getType());
                        return orders;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Orders>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onErroe------->>>>:", e.toString());
                        mView.getOrdersListFail(e.toString());
                    }

                    @Override
                    public void onNext(List<Orders> orders) {
                        Log.d("get-----getget", orders.get(0).nxRoQuantity + orders.get(0).nxRoStandard);
                        mView.getOrdersListSuccess(orders);
                    }
                });


    }

    @Override
    public void printOrder(Integer orderId, String orderWeight) {
        HttpManager.getInstance().request(HttpManager.getInstance().getApi(OrdersApi.class).printOrder(orderId, orderWeight))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.printOrderFail(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        mView.printOrderSuccess();
                    }
                });

    }


}
