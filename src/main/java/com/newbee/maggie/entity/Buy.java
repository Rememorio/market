package com.newbee.maggie.entity;

public class Buy {
    //商品id
    private Integer cmId;
    //用户id
    private Integer userId;
    //订单id
    private Integer orderId;
    //交易时间
    private String timeOfTransaction;

    public Buy(Integer cmId, Integer userId, Integer orderId, String timeOfTransaction) {
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

    public String getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(String timeOfTransaction) {
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
