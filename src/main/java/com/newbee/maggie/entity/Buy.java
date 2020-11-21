package com.newbee.maggie.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Buy {
    //商品id
    private Integer cmId;
    //用户id
    private Integer userId;
    //订单id
    private Integer orderId;
    //预定时间
    private String timeOfReserve;
    //交易时间
    private String timeOfTransaction;

    public Buy(Integer cmId, Integer userId, Integer orderId, String timeOfReserve, String timeOfTransaction) {
        this.cmId = cmId;
        this.userId = userId;
        this.orderId = orderId;
        this.timeOfReserve = timeOfReserve;
        this.timeOfTransaction = timeOfTransaction;
    }

    public Buy(Integer cmId, Integer userId) {
        this.cmId = cmId;
        this.userId = userId;
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String timeOfTransaction = ft.format(date);
        this.timeOfTransaction = timeOfTransaction;
    }

    public Integer getCmId() {
        return cmId;
    }

    public void setCmId(Integer cmId) {
        this.cmId = cmId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(String timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
    }

    public String getTimeOfReserve() {
        return timeOfReserve;
    }

    public void setTimeOfReserve(String timeOfReserve) {
        this.timeOfReserve = timeOfReserve;
    }

    @Override
    public String toString() {
        return "Buy{" +
                "cmId=" + cmId +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", timeOfTransaction=" + timeOfTransaction +
                '}';
    }
}
