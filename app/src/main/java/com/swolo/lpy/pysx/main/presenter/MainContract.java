package com.swolo.lpy.pysx.main.presenter;

import com.swolo.lpy.pysx.main.modal.ComuFatherGoods;
import com.swolo.lpy.pysx.main.modal.ComuGoods;
import com.swolo.lpy.pysx.main.modal.Orders;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public interface MainContract {



    interface FatherGoodsView{
        void getFatherGoodsListSuccess(List<ComuFatherGoods> comuFatherGoods);
        void getFatherGoodsListFail(String error);
    }

    interface FatherGoodsPresenter{
        void getFatherGoodsList(String comId, String type);
    }


    interface GoodsView{
        void getGoodsListSuccess(List<ComuGoods> comuGoods);
        void getGoodsListFail(String error);
    }
    interface  GoodsPresenter{
        void getGoodsList(String fatherId, String type);
    }

    interface OrdersView{
        void getOrdersListSuccess(List<Orders> orders);
        void getOrdersListFail(String error);
        void printOrderSuccess();
        void printOrderFail(String error);
    }
    interface  OrdersPresenter{
        void getOrdersList(String goodsId);
        void printOrder(Integer orderId, String orderWeight);
    }


}
