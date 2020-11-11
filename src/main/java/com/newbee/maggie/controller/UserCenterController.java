package com.newbee.maggie.controller;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.service.UserCenterService;
import com.newbee.maggie.util.CommodityNotFoundException;
import com.newbee.maggie.util.ParamNotFoundException;
import com.newbee.maggie.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userCenter")
public class UserCenterController {
    @Autowired
    private UserCenterService userCenterService;

    /**
     * 根据用户名返回用户信息
     * @param usernameMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfoByNickname", method = RequestMethod.POST)
    private Map<String, Object> userInfoByUserName(@RequestBody Map<String, String> usernameMap) throws ParamNotFoundException, UserNotFoundException {
        String username = usernameMap.get("nickname");//获取昵称
        if (username == null) {//如果没有这个昵称
            throw new ParamNotFoundException("nickname参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByNickname(username);
        if(user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("errorCode", 0);
        map.put("data", user);
        return map;
    }

    /**
     * 根据用户id返回用户信息
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfoByUserId", method = RequestMethod.POST)
    private  Map<String, Object> userInfoByUserId(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException {
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("errorCode", 0);
        map.put("data", user);
        return map;
    }

//    public String doPost(String url, Map<String,String> map) throws Exception {
//        String result = null;
//        HttpClient httpClient = new SSLClient();
//        HttpPost httpPost = new HttpPost(url);
//        //设置参数
//        List<NameValuePair> list = new ArrayList<NameValuePair>();
//        Iterator iterator = map.entrySet().iterator();
//        while(iterator.hasNext()){
//            Entry<String,String> elem = (Entry<String, String>) iterator.next();
//            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
//        }
//        if(list.size() > 0){
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
//            httpPost.setEntity(entity);
//        }
//        HttpResponse response = httpClient.execute(httpPost);
//        if(response != null){
//            HttpEntity resEntity = response.getEntity();
//            if(resEntity != null){
//                result = EntityUtils.toString(resEntity, "UTF-8");
//            }
//        }
//        return result;
//    }
//
//    public String doGet(String url, Map<String,String> map) throws Exception{
//        String result = null;
//        HttpClient httpClient = new SSLClient();
//        String param="";
//        for(String nameKey:map.keySet()){
//            param += nameKey+"="+map.get(nameKey)+"&";
//        }
//        param = param.substring(0,param.length()-1);
//        String urlNameString = url + "?" + param;
//        HttpGet httpGet = new HttpGet(urlNameString);
//        HttpResponse response = httpClient.execute(httpGet);
//        if(response != null){
//            HttpEntity resEntity = response.getEntity();
//            if(resEntity != null){
//                result = EntityUtils.toString(resEntity, "UTF-8");
//            }
//        }
//        return result;
//    }

//    private Map<String, Object> _getOpenId(String code){
//        Map<String, Object> ret = new HashMap<>();
//        // 组装参数*****
//        Map<String, String> urlData= new HashMap<String, String>();
//        urlData.put("appid",appid);//小程序id
//        urlData.put("secret",appKey);//小程序key
//        urlData.put("grant_type","authorization_code");//固定值这样写就行
//        urlData.put("js_code",code);//小程序传过来的code
//        HttpsClientUtil httpsClientUtil = new HttpsClientUtil();
//        Object data_deserialize = null;
//        try {
//            //code2OpenidUrl "https://api.weixin.qq.com/sns/jscode2session";
//            String dataStr = httpsClientUtil.doGet(code2OpenidUrl, urlData);
//            data_deserialize = JSONUtil.deserialize(dataStr);
//        }catch(Exception ex){
//            ret.put("success", false);
//            ret.put("msg", "_getOpenId_未知异常");
//            ret.put("message", ex);
//            return ret;
//        }
//        Map<String, String> data=  (Map<String, String>)data_deserialize;
//        if( data.containsKey("errcode") ){
//            ret.put("success", false);
//            ret.put("msg", data.containsKey("errcode"));
//            ret.put("message", data.containsKey("errmsg"));
//        }else{
//            ret.put("success", true);
//            ret.put("result",data);
//        }
//        return ret;
//    }

//    /**
//     * 个人中心-授权
//     */
//    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
//    private Map<String, Object> getUserInfo() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        return map;
//    }

    /**
     * 根据用户id查询是否具有管理员权限
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/getIsAdmin", method = RequestMethod.POST)
    private Map<String, Object> getIsAdmin(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException {
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        int authority = user.getAuthority();
        Map<String, Integer> auth = new HashMap<String, Integer>();//封装用户权限信息
        auth.put("authority", authority);
        map.put("errorCode", 0);
        map.put("data", auth);
        return map;
    }

    /**
     * 根据用户id查询用户已买到的
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     * @throws CommodityNotFoundException
     */
    @RequestMapping(value = "/userBoughts", method = RequestMethod.POST)
    private Map<String, Object> userBoughts(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException, CommodityNotFoundException {
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        List<Buy> buyList= new ArrayList<Buy>();
        buyList = userCenterService.getBuyByUserId(userId);//获取这个用户的buy列表
        List<Commodities> commoditiesList= new ArrayList<Commodities>();//存储商品信息的List
        List<HashMap<String, Object>> orderMapList = new ArrayList<HashMap<String, Object>>();//存储商品交易订单id和交易时间的List
        for(Buy buy: buyList) {//对于这个List<buy>里面的每个buy
            //以下为商品部分
            Integer cmId = buy.getCmId();//获取这个buy的商品id
            Commodity commodity = userCenterService.getCommodityByCmId(cmId);//根据商品id获取这个商品的详情
            if (commodity == null) {//应该不会没有的叭，不过写上好过没写
                throw new CommodityNotFoundException("商品不存在");
            }
            List<HashMap<String, Object>> urlsMapList = new ArrayList<HashMap<String, Object>>();//存储商品图片的List
            //以下为图片url(s)部分
            //处理图片url，以","作为分隔符
            String pictureUrl = commodity.getPictureUrl();
            if (pictureUrl.contains(",")) {//如果有","，即不止一个url
                String[] pictureUrls = pictureUrl.split(",");
                Commodities commodities = new Commodities(commodity, pictureUrls);
                commoditiesList.add(commodities);
            } else {//没有","，即只有一个url
                String[] pictureUrls = new String[]{pictureUrl};
                Commodities commodities = new Commodities(commodity, pictureUrls);
                commoditiesList.add(commodities);
            }

            //以下为订单id和交易时间部分
            Integer orderId = buy.getOrderId();
            String timeOfTransaction = buy.getTimeOfTransaction();
            HashMap<String, Object> orderMap = new HashMap<String, Object>();
            orderMap.put("cmId", buy.getCmId());
            orderMap.put("orderId", buy.getOrderId());
            orderMap.put("timeOfTransaction", buy.getTimeOfTransaction());
            orderMapList.add(orderMap);
        }
        //封装信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commodityList", commoditiesList);
        map.put("orderList", orderMapList);
        map.put("errorCode", 0);
        return map;
    }

    @RequestMapping(value = "/userFavors", method = RequestMethod.POST)
    private Map<String, Object> userFavors(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException, CommodityNotFoundException {
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        List<Collect> collectList = new ArrayList<Collect>();
        collectList = userCenterService.getCollectByUserId(userId);//获取这个用户的收藏列表
        List<Commodities> commoditiesList= new ArrayList<Commodities>();//存储商品信息的List
        for (Collect collect: collectList) {//对于这个List<Collect>的每个collect
            //以下为商品部分
            Integer cmId = collect.getCmId();//获取这个buy的商品id
            Commodity commodity = userCenterService.getCommodityByCmId(cmId);//根据商品id获取这个商品的详情
            if (commodity == null) {//应该不会没有的叭，不过写上好过没写
                throw new CommodityNotFoundException("商品不存在");
            }
            List<HashMap<String, Object>> urlsMapList = new ArrayList<HashMap<String, Object>>();//存储商品图片的List
            HashMap<String, Object> pictureMap = new HashMap<String, Object>();//将同一个商品的id和商品图片的List封装起来
            //以下为图片url(s)部分
            //处理图片url，以","作为分隔符
            String pictureUrl = commodity.getPictureUrl();
            if (pictureUrl.contains(",")) {//如果有","，即不止一个url
                String[] pictureUrls = pictureUrl.split(",");
                Commodities commodities = new Commodities(commodity, pictureUrls);
                commoditiesList.add(commodities);
            } else {//没有","，即只有一个url
                String[] pictureUrls = new String[]{pictureUrl};
                Commodities commodities = new Commodities(commodity, pictureUrls);
                commoditiesList.add(commodities);
            }
        }
        //封装信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commodityList", commoditiesList);
        map.put("errorCode", 0);
        return map;
    }

    @RequestMapping(value = "/userBookings", method = RequestMethod.POST)
    private Map<String, Object> userBookings(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException, CommodityNotFoundException {
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        List<Reserve> reserveList = new ArrayList<Reserve>();
        reserveList = userCenterService.getReserveByUserId(userId);//获取这个用户的预定列表
        List<Commodities> commoditiesList= new ArrayList<Commodities>();//存储商品信息的List
        List<HashMap<String, Object>> reserveMapList = new ArrayList<HashMap<String, Object>>();//存储商品预定订单id和预定时间的List
        for (Reserve reserve: reserveList) {//对于这个List<Reserve>的每个reserve
            //以下为商品部分
            Integer cmId = reserve.getCmId();//获取这个buy的商品id
            Commodity commodity = userCenterService.getCommodityByCmId(cmId);//根据商品id获取这个商品的详情
            if (commodity == null) {//应该不会没有的叭，不过写上好过没写
                throw new CommodityNotFoundException("商品不存在");
            }
            List<HashMap<String, Object>> urlsMapList = new ArrayList<HashMap<String, Object>>();//存储商品图片的List
            HashMap<String, Object> pictureMap = new HashMap<String, Object>();//将同一个商品的id和商品图片的List封装起来
            //以下为图片url(s)部分
            //处理图片url，以","作为分隔符
            String pictureUrl = commodity.getPictureUrl();
            if (pictureUrl.contains(",")) {//如果有","，即不止一个url
                String[] pictureUrls = pictureUrl.split(",");
                Commodities commodities = new Commodities(commodity, pictureUrls);
                commoditiesList.add(commodities);
            } else {//没有","，即只有一个url
                String[] pictureUrls = new String[]{pictureUrl};
                Commodities commodities = new Commodities(commodity, pictureUrls);
                commoditiesList.add(commodities);
            }

            //以下为预定订单id和预定时间部分
            Integer reserveId = reserve.getReserveId();
            String reserveTime = reserve.getReserveTime();
            HashMap<String, Object> reserveMap = new HashMap<String, Object>();
            reserveMap.put("cmId", reserve.getCmId());
            reserveMap.put("reserveId", reserve.getReserveId());
            reserveMap.put("reserveTime", reserve.getReserveTime());
            reserveMapList.add(reserveMap);
        }
        //封装信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commodityList", commoditiesList);
        map.put("reserveList", reserveMapList);
        map.put("errorCode", 0);
        return map;
    }
}