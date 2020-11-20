package com.newbee.maggie.web;

public class WxAuthVO {
    private String openid;
    private String session_key;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    @Override
    public String toString() {
        return "WxAuthVO{" +
                "openId='" + openid + '\'' +
                ", sessionKey='" + session_key + '\'' +
                '}';
    }
}
