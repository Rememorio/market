package com.newbee.maggie.entity;

import java.util.Date;

public class Reserve {
    //商品id
    private Integer cmId;
    //用户id
    private Integer userId;
    //预定的id
    private Integer reserveId;
    //预定的时间
    private Date reserveTime;

    public Reserve(Integer cmId, Integer userId, Integer reserveId, Date reserveTime) {
        this.cmId = cmId;
        this.userId = userId;
        this.reserveId = reserveId;
        this.reserveTime = reserveTime;
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

    public Integer getReserveId() {
        return reserveId;
    }

    public void setReserveId(Integer reserveId) {
        this.reserveId = reserveId;
    }

    public Date getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(Date reserveTime) {
        this.reserveTime = reserveTime;
    }

    @Override
    public String toString() {
        return "Reserve{" +
                "cmId=" + cmId +
                ", userId=" + userId +
                ", reserveId=" + reserveId +
                ", reserveTime=" + reserveTime +
                '}';
    }
}
