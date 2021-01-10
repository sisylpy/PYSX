package com.swolo.lpy.pysx.main.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swolo.lpy.pysx.api.GoodsApi;
import com.swolo.lpy.pysx.http.HttpManager;
import com.swolo.lpy.pysx.main.modal.ComuGoods;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ComuGoodsPresenterImpl implements MainContract.GoodsPresenter {

    private MainContract.GoodsView mView;
    private static String TAG = "ComuGoodsPresenterImpl---";

    public ComuGoodsPresenterImpl(MainContract.GoodsView mView) {
        this.mView = mView;
    }

    @Override
    public void getGoodsList(String fatherId, String type) {
    HttpManager.getInstance().request(HttpManager.getInstance().getApi(GoodsApi.class).weighingGetGoods(fatherId,type))
            .map(new Func1<String, List<ComuGoods>>() {
                @Override
                public List<ComuGoods> call(String s) {
                    List<ComuGoods> goods = new Gson().fromJson(s, new TypeToken<List<ComuGoods>>() {}.getType());
                    return goods;
                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<List<ComuGoods>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.getGoodsListFail(e.toString());
                }

                @Override
                public void onNext(List<ComuGoods> comuGoods) {
                    Log.d(TAG, comuGoods.get(0).nxCgGoodsName);
                    mView.getGoodsListSuccess(comuGoods);
                }
            });


    }
}
