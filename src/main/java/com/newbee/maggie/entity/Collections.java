package com.newbee.maggie.entity;

public class Collections {
    //用户id
    private Integer userId;
    //商品id
    private  Integer cmId;

    public Collections(Integer userId, Integer cmId) {
        this.userId = userId;
        this.cmId = cmId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCmId() {
        return cmId;
    }

    public void setCmId(Integer cmId) {
        this.cmId = cmId;
    }

    @Override
    public String toString() {
        return "Collections{" +
                "userId=" + userId +
                ", cmId=" + cmId +
                '}';
    }
}
