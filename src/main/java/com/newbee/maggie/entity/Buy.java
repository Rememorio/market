package com.newbee.maggie.entity;

import java.util.Date;

public class Buy {
    //商品id
    private Integer cmId;
    //用户id
    private Integer userId;
    //订单id
    private Integer orderId;
    //交易时间
    private Date timeOfTransaction;

    public Buy(Integer cmId, Integer userId, Integer orderId, Date timeOfTransaction) {
        this.cmId = cmId;
        this.userId = userId;
        this.orderId = orderId;
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

    public Date getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(Date timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
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
