package com.newbee.maggie.util;

import org.springframework.lang.Nullable;

/**
 * Description: 成功码
 */
public enum MsgSuccess {
    /**
     *返回数据成功
     */
    OK(200, "返回数据成功");

    private final int code;

    private final String succesMsg;


    MsgSuccess(int code, String succesMsg) {
        this.code = code;
        this.succesMsg = succesMsg;
    }


    /**
     * 成功码
     * @return
     */
    public int code() {
        return this.code;
    }

    /**
     * 获得成功码的描述
     * @return
     */
    public String getSuccesMsg() {
        return this.succesMsg;
    }


    /**
     *
     * @param statusCode
     * @return
     */
    public static MsgSuccess valueOf(int statusCode) {
        MsgSuccess error = resolve(statusCode);
        if (error == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return error;
    }

    /**
     * 成功码
     * @param statusCode
     * @return
     */
    @Nullable
    public static MsgSuccess resolve(int statusCode) {
        for (MsgSuccess error : values()) {
            if (error.code == statusCode) {
                return error;
            }
        }
        return null;
    }
}
