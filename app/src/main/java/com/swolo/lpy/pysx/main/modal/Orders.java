package com.swolo.lpy.pysx.main.modal;

import java.io.Serializable;

public class Orders implements Serializable{


    /**
     *  饭馆订单id
     */
    public Integer nxRestrauntOrdersId;
    /**
     *  饭馆订单nx商品id
     */
    public Integer nxRoNxGoodsId;

    public Integer getNxRoRestrauntId() {
        return nxRoRestrauntId;
    }

    public void setNxRoRestrauntId(Integer nxRoRestrauntId) {
        this.nxRoRestrauntId = nxRoRestrauntId;
    }

    /**
     *  饭馆订单商品父id

     */
    public Integer nxRoNxGoodsFatherId;
    /**
     *  饭馆区域商品id
     */
    public Integer nxRoComGoodsId;
    /**
     *  区域父级商品id
     */
    public Integer nxRoComGoodsFatherId;
    /**
     *  饭馆id
     */
    public Integer nxRoResComGoodsId;
    /**
     *  饭馆商品价格
     */
    public String nxRoResComGoodsPrice;
    /**
     *  部门订单申请数量
     */
    public String nxRoQuantity;
    /**
     *  部门订单申请规格
     */
    public String nxRoStandard;
    /**
     *  部门订单申请备注
     */
    public String nxRoRemark;
    /**
     *  部门订单重量
     */
    public String nxRoWeight;
    /**
     *  部门订单商品单价
     */
    public String nxRoPrice;
    /**
     *  部门订单申请商品小计
     */
    public String nxRoSubtotal;
    /**
     *  部门订单部门id
     */
    public Integer nxRoRestrauntId;
    /**
     *
     */
    public Integer nxRoRestrauntFatherId;
    /**
     *  部门订单批发商id
     */
    public Integer nxRoCommunityId;
    /**
     *  部门商品采购员id
     */
    public Integer nxRoPurchaseUserId;
    /**
     *  部门订单账单id
     */
    public Integer nxRoBillId;
    /**
     *  部门订单申请商品状态
     */
    public Integer nxRoStatus;
    /**
     *  部门订单订货用户id
     */
    public Integer nxRoOrderUserId;
    /**
     *  部门订单商品称重用户id
     */
    public Integer nxRoPickUserId;
    /**
     *  部门订单商品输入单价用户id
     */
    public Integer nxRoAccountUserId;
    /**
     *  部门订单商品进货状态
     */
    public Integer nxRoBuyStatus;
    /**
     *  部门订单申请时间
     */
    public String nxRoApplyDate;
    /**
     *  部门订单送达时间
     */
    public String nxRoArriveDate;
    /**
     *  订单采购商品id
     */
    public Integer nxRoPurchaseGoodsId;
    /**
     *
     */
    public String nxRoArriveOnlyDate;
    /**
     *
     */
    public String nxRoApplyFullTime;
    /**
     *
     */
    public String nxRoOperationTime;
    /**
     *  星期几
     */
    public String nxRoArriveWhatDay;
    /**
     *
     */
    public Integer nxRoIsAgent;
    /**
     *  本年第几周
     */
    public Integer nxRoArriveWeeksYear;
    /**
     *
     */
    public String nxRoApplyOnlyTime;

    public Integer nxRoSellType;
    public String nxRoExpectPrice;
    public String nxRoScale;

    public String nxRoCostPrice;


    public Boolean showDate = true;

    public String getNxRoWeight() {
        return nxRoWeight;
    }

    public void setNxRoWeight(String nxRoWeight) {
        this.nxRoWeight = nxRoWeight;
    }

    public Boolean  isWeeks = true;
    public Boolean hasChoice =  true;

    public Boolean getHasChoice() {
        return hasChoice;
    }

    public void setHasChoice(Boolean hasChoice) {
        this.hasChoice = hasChoice;
    }

    public ComuGoods nxCommunityGoodsEntity;

    public Restraunt  nxRestrauntEntity;
}
