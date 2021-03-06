package com.newbee.maggie.entity;

import java.util.Arrays;

public class Commodities {
    //商品id
    private Integer cmId;
    //商品名称
    private String name;
    //商品分类
    /**
     * 0-置空[不筛选类别]，1-电子产品，2-讲座票，3-校园网，4-日用品，5-书籍，6-文具，7-美妆，8-零食，9-其他
     */
    private int classify;
    //描述
    private String details;
    //价格
    private Double price;
    //用户id
    private Integer userId;
    //地址
    /**
     * 0-置空[不筛选地址]，1-大学城，2-五山，3-国际，4-其他
     */
    private int address;
    //图片url
    private String[] pictureUrls;
    //上传时间
    private String date;
    //是否全新
    /**
     * 0-非全新，1-全新
     */
    private int isNew;
    //状态
    /**
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定，5-已付款售出，6-被举报
     */
    private int state;

    public Commodities(Commodity commodity, String[] pictureUrls) {
        this.pictureUrls = pictureUrls;
        this.address = commodity.getAddress();
        this.classify = commodity.getClassify();
        this.cmId = commodity.getCmId();
        this.date = commodity.getLaunchTime();
        this.details = commodity.getDetails();
        this.isNew = commodity.getIsNew();
        this.name = commodity.getName();
        this.price = commodity.getPrice();
        this.state = commodity.getState();
        this.userId = commodity.getUserId();
    }

    public Integer getCmId() {
        return cmId;
    }

    public void setCmId(Integer cmId) {
        this.cmId = cmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String[] getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(String[] pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Commodities{" +
                "cmId=" + cmId +
                ", name='" + name + '\'' +
                ", classify=" + classify +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", userId=" + userId +
                ", address=" + address +
                ", pictureUrls=" + Arrays.toString(pictureUrls) +
                ", date='" + date + '\'' +
                ", isNew=" + isNew +
                ", state=" + state +
                '}';
    }
}
