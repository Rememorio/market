package com.newbee.maggie.web;
/**
 * Description:j
 * <p>
 *  jwt的头部承载两部分信息：
 * 1,声明类型，这里是jwt
 * 2,声明加密的算法 通常直接使用 HMAC SHA256
 * </p>
 */
public class TokenHeader {
    private String typ;//声明类型
    private String alg;//声明加密的算法 通常直接使用 HMAC SHA256

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }
}
