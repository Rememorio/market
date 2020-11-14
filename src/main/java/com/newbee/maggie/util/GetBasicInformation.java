package com.newbee.maggie.util;

import net.sf.json.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.Map;

// 通过openid获取用户的信息，这个看你需要获取用户的哪些信息
// 微信官方的获取unionid机制  用户信息
// https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140839
public class GetBasicInformation {
    // 网页授权接口
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    public static Map<String, String> getAccessToken(String access_token, String openid) {
        String requestUrl = GetPageAccessTokenUrl.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        HttpClient client = null;
        Map<String, String> result = new HashMap<String, String>();
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSONObject.fromObject(response);

//            String accessToken = String.valueOf(OpenidJSONO.get("access_token"));
            String subscribe = String.valueOf(OpenidJSONO.get("subscribe"));
            String nickname = new String(String.valueOf(OpenidJSONO.get("nickname")).getBytes("ISO8859-1"),"UTF-8");
            String sex = String.valueOf(OpenidJSONO.get("sex"));
            String language = String.valueOf(OpenidJSONO.get("language"));
            String city = new String(String.valueOf(OpenidJSONO.get("city")).getBytes("ISO8859-1"),"UTF-8");
            String province = new String(String.valueOf(OpenidJSONO.get("province")).getBytes("ISO8859-1"),"UTF-8");
            String country = new String(String.valueOf(OpenidJSONO.get("country")).getBytes("ISO8859-1"),"UTF-8");
            String headimgurl = String.valueOf(OpenidJSONO.get("headimgurl"));
            String subscribeTime = String.valueOf(OpenidJSONO.get("subscribe_time"));
            String unionid = String.valueOf(OpenidJSONO.get("unionid"));
//            String accessToken =new String(String.valueOf(OpenidJSONO.get("access_token")).getBytes("ISO8859-1"),"UTF-8");
//            String openid =new String(String.valueOf(OpenidJSONO.get("openid")).getBytes("ISO8859-1"),"UTF-8");

//            String openid = String.valueOf(OpenidJSONO.get("openid"));
//            result.put("accessToken", accessToken);
            result.put("subscribe", subscribe);
            result.put("nickname", nickname);
            result.put("sex", sex);
            result.put("language", language);
            result.put("city", city);
            result.put("province", province);
            result.put("country", country);
            result.put("headimgurl", headimgurl);
            result.put("subscribeTime", subscribeTime);
            result.put("unionid", unionid);

//            System.out.println(accessToken+"==================="+unionid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
}
