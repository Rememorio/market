package com.newbee.maggie.web;

/**
 * Description: 请求公共实体类
 */
public class RequestVO {
    private String token;
    private Long sceneryId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getSceneryId() {
        return sceneryId;
    }

    public void setSceneryId(Long sceneryId) {
        this.sceneryId = sceneryId;
    }

    @Override
    public String toString() {
        return "RequestParamVO{" +
                "token='" + token + '\'' +
                ", sceneryId=" + sceneryId +
                '}';
    }
}
