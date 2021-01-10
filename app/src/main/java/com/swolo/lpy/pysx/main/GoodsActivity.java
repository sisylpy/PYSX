package com.swolo.lpy.pysx.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.swolo.lpy.pysx.R;
import com.swolo.lpy.pysx.main.adapter.GoodsViewAdapter;
import com.swolo.lpy.pysx.main.modal.ComuFatherGoods;
import com.swolo.lpy.pysx.main.modal.ComuGoods;
import com.swolo.lpy.pysx.main.presenter.ComuGoodsPresenterImpl;
import com.swolo.lpy.pysx.main.presenter.MainContract;
import com.swolo.lpy.pysx.ui.BaseActivity;
import com.swolo.lpy.pysx.util.ActivityUtil;
import com.swolo.lpy.pysx.util.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.swolo.lpy.pysx.main.DeviceConnFactoryManager.ACTION_CONN_STATE;
import static com.swolo.lpy.pysx.main.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static com.swolo.lpy.pysx.main.gp.Constant.ACTION_USB_PERMISSION;

public class GoodsActivity extends BaseActivity implements MainContract.GoodsView{

    public final static int CHOOSE_FATHER_GOODS_RESULT_CODE = 0x11;

    ComuFatherGoods comuFatherGoods;
    String fatherId;


    private RecyclerView goodsView ;

    private ComuGoodsPresenterImpl comuGoodsPresenter;
    private GoodsViewAdapter goodsViewAdapter;
    private String goodsType;


    @Override
    protected void initView() {

        goodsView = findViewById(R.id.goods_list);
    }

    @Override
    protected void initData() {
        comuFatherGoods = (ComuFatherGoods)getIntent().getSerializableExtra("comuFatherGoods");
        goodsType = getIntent().getStringExtra("type");


        setTitle(comuFatherGoods.nxCfgFatherGoodsName);
        fatherId = comuFatherGoods.nxCommunityFatherGoodsId.toString();
        goodsViewAdapter = new GoodsViewAdapter();
        comuGoodsPresenter = new ComuGoodsPresenterImpl(this);

    }

    @Override
    protected void bindAction() {
        goodsViewAdapter.setOnClickGoodsListener(new GoodsViewAdapter.OnGoodsClickListener() {
            @Override
            public void onItemClick(ComuGoods comuGoods) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", comuGoods);
                ActivityUtil.next(GoodsActivity.this, com.swolo.lpy.pysx.main.OrdersPrintActivity.class ,bundle, com.swolo.lpy.pysx.main.OrdersActivity.CHOOSE_GOODS_RESULT_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0X33:{
                //刷新页面，获取更新个人资料后的内容
                if(resultCode==1){//resultCode需要与SecondActivity中的resultCode一致
                    //获取返回数据，获取用户名
                    showLoading();
                    comuGoodsPresenter.getGoodsList(fatherId,goodsType);
                }
                break;
            }
            //……
            default:
                break;
        }
    }
    @Override
    protected void setView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        goodsView.setLayoutManager(gridLayoutManager);
        goodsView.setAdapter(goodsViewAdapter);
        showLoading();
        comuGoodsPresenter.getGoodsList(fatherId, goodsType);
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_weigh;
    }


    @Override
    public void getGoodsListSuccess(List<ComuGoods> comuGoods) {
        stopLoading();
        goodsViewAdapter.setData(comuGoods);
    }

    @Override
    public void getGoodsListFail(String error) {
        stopLoading();
        showToast(error);
//        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
            ActivityUtil.goBackWithResult(GoodsActivity.this,1,null);
    }






}
