package com.newbee.maggie.util;

import org.springframework.lang.Nullable;

/**
 * Description: 错误码
 */
public enum MsgError  {
    /**
     *返回数据为空
     */
    COMMON_EMPTY(1000, "返回数据为空"),

    /**
     *微信签名信息不一致
     */
    WX_SIGNATURE(1001, "微信签名信息不一致");

    private final int code;

    private final String errorMsg;


    MsgError(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    /**
     * 错误码
     * @return
     */
    public int code() {
        return this.code;
    }

    /**
     * 获得错误码的描述
     * @return
     */
    public String getErrorMsg() {
        return this.errorMsg;
    }


    /**
     *
     * @param statusCode
     * @return
     */
    public static MsgError valueOf(int statusCode) {
        MsgError error = resolve(statusCode);
        if (error == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return error;
    }

    /**
     * 解析错误码
     * @param statusCode
     * @return
     */
    @Nullable
    public static MsgError resolve(int statusCode) {
        for (MsgError error : values()) {
            if (error.code == statusCode) {
                return error;
            }
        }
        return null;
    }
}
