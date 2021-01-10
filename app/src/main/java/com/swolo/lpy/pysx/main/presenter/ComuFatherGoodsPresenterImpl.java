package com.swolo.lpy.pysx.main.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swolo.lpy.pysx.api.GoodsApi;
import com.swolo.lpy.pysx.http.HttpManager;
import com.swolo.lpy.pysx.main.modal.ComuFatherGoods;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public class ComuFatherGoodsPresenterImpl implements MainContract.FatherGoodsPresenter {

    private MainContract.FatherGoodsView mView;

    public ComuFatherGoodsPresenterImpl(MainContract.FatherGoodsView view) {
        this.mView = view;
    }

    @Override
    public void getFatherGoodsList(String comId, String type) {
        HttpManager.getInstance().request(HttpManager.getInstance().getApi(GoodsApi.class).weighingGetOrderGoodsType(comId, type))
                .map(new Func1<String, List<ComuFatherGoods>>() {
                    @Override
                    public List<ComuFatherGoods> call(String s) {
                        List<ComuFatherGoods> suppliers = new Gson().fromJson(s,new TypeToken<List<ComuFatherGoods>>() {}.getType());
                        System.out.println(suppliers);
                        System.out.println("=======");
                        return suppliers;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.io())
                  .subscribe(new Subscriber<List<ComuFatherGoods>>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {
                          Log.d("onErroe------->>>>:", e.toString());
                          mView.getFatherGoodsListFail(e.getMessage());
                      }

                      @Override
                      public void onNext(List<ComuFatherGoods> comuFatherGoodsList) {
                          Log.i("http______get", comuFatherGoodsList.get(0).nxCfgFatherGoodsName);
                          mView.getFatherGoodsListSuccess(comuFatherGoodsList);
                      }
                  });
    }
}
