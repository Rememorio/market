package com.newbee.maggie.web;

/**
 * 微信信息相关常量
 */
public class WxConsts {
    public static String APP_ID = "wxc4eb5a19612df490";
    public static String APP_SECRET = "82e99a80db3f4558f68881626417ad75";
    public static String BASE_URL = "https://api.weixin.qq.com";

    public static String API_SESSION_KEY = BASE_URL + "/sns/jscode2session?appid=%s&secret=%s&js_code={0}&grant_type=authorization_code";
}