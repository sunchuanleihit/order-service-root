package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class GoodsInfoDto implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 5767299471780521543L;
    private int goodsId;
    private String goodsName;
    private String goodsImage;

    public GoodsInfoDto() {
        super();
    }

    public GoodsInfoDto(int goodsId, String goodsName, String goodsImage) {
        super();
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsImage = goodsImage;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    @Override
    public String toString() {
        return "GoodsInfoDto [goodsId=" + goodsId + ", goodsName=" + goodsName + ", goodsImage=" + goodsImage + "]";
    }

}
