package com.newbee.maggie.web;

import java.io.Serializable;

/**
 * 用户信息 微信登录验证VO
 */
public class WxLoginVO implements Serializable {
    /**
     * 对应小程序获取用户信息的encryptedData字段
     */
    private String encryptedData;

    /**
     * 对应小程序获取用户信息的iv字段
     */
    private String iv;

    /**
     * 对应小程序获取用户信息的rawData字段
     */
    private String rawData;

    /**
     * 对应小程序获取用户信息的signature字段
     */
    private String signature;

    /**
     * 对应小程序登录的时候反的code字段
     */
    private String code;

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "WxLoginVO{" +
                "encryptedData='" + encryptedData + '\'' +
                ", iv='" + iv + '\'' +
                ", rawData='" + rawData + '\'' +
                ", signature='" + signature + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}