package com.newbee.maggie.entity;

//由于不知道为什么Collection类与java.lang.Collection有冲突，故改名为Collect
public class Collect {
    //用户id
    private Integer userId;
    //商品id
    private  Integer cmId;

    public Collect(Integer userId, Integer cmId) {
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
        return "Collect{" +
                "userId=" + userId +
                ", cmId=" + cmId +
                '}';
    }
}
