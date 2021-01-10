package com.swolo.lpy.pysx.main.modal;

import java.io.Serializable;
import java.util.List;

public class ComuGoods implements Serializable{


    /**
     *  批发商商品id
     */
    public Integer nxCommunityGoodsId;
    /**
     *  批发商id
     */
    public Integer nxCgCommerceId;

    /**
     * 社区id
     */
    public Integer nxCgCommunityId;



    /**
     *  商品状态
     */
    public Integer nxCgGoodsStatus;

    /**
     *  是否称重
     */
    public Integer nxCgGoodsIsWeight;
    /**
     * 价格
     */
    public String nxCgGoodsPrice;
    public String nxCgGoodsPriceInteger;
    public String nxCgGoodsTwoPrice;
    public String nxCgGoodsThreePrice;
    /**
     * 价格小数点
     */
    public String nxCgGoodsPriceDecimal;

    public String nxCgNxGoodsFilePath;
    public Integer nxCgGoodsSellType;

    public Integer nxCgCfgGoodsFatherId;


    /**
     *  商品id
     */
    public Integer nxCgNxGoodsId;
    /**
     * 父类id
     */
    public Integer nxCgNxFatherId;
    public String nxCgNxFatherName;
    /**
     * image
     */
    public String nxCgNxFatherImg;
    public Integer nxCgNxGrandId;
    public String nxCgNxGrandName;

    public Integer nxCgNxGreatGrandId;
    public String nxCgNxGreatGrandName;

    public Integer nxCgGoodsTotalHits;
    /**
     *  采购数量
     */
    public String nxCgPurchaseQuantity;

    public Integer nxCgGoodsBuyType;
    public Integer nxCgBuyPurchaseUserId;

    public Integer nxCgBuyAppId;

    public Integer nxCgBuyStatus;

    public Integer nxCgSupplierId;
    public String nxCgBuyingPrice;
    public Integer nxCgGoodsType;

    public String nxCgGoodsName;

    public String nxCgGoodsDetail;

    public String nxCgGoodsStandardname;
    public String nxCgGoodsBrand;
    public String nxCgGoodsPlace;

    public String nxCgGoodsStock;

    public String nxCgGoodsPinyin;

    public String nxCgGoodsPy;
    public Integer nxCgSellType;
    public Integer nxCgCustomerPrice;



    public Boolean isSelected = false;


    public String nxCgGoodsStandardWeight;
    public String nxCgNxGoodsFatherColor;
    public Integer nxCgPullOff;

    public String nxCgExpectGrossProfit;
    public String nxCgRealityGrossProfit;

    
    
}
