package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Buy;
import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.service.UserCenterService;
import com.newbee.maggie.util.CommodityNotFoundException;
import com.newbee.maggie.util.ParamNotFoundException;
import com.newbee.maggie.util.UserNotFoundException;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
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
    private Map<String, Object> userBoughts(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, CommodityNotFoundException {
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        List<Buy> buyList= new ArrayList<Buy>();
        buyList = userCenterService.getBuyByUserId(userId);//获取这个用户的buy列表
        List<Commodities> commoditiesList= new ArrayList<Commodities>();//存储商品信息的List
        List<HashMap<String, Object>> pictureMapList = new ArrayList<HashMap<String, Object>>();//存储商品对应的图片url(s)的List
        List<HashMap<String, Object>> orderMapList = new ArrayList<HashMap<String, Object>>();//存储商品交易订单id和交易时间的List
        for(Buy buy: buyList) {//对于这个List<buy>里面的每个buy
            //以下为商品部分
            Integer cmId = buy.getCmId();//获取这个buy的商品id
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
                //用另外一个Map存储图片的url
                for (int i = 0; i < pictureUrls.length; i++) {
                    HashMap<String, Object> urlsMap = new HashMap<String, Object>();
                    urlsMap.put("urlId", i);
                    urlsMap.put("urlSrc", pictureUrls[i]);
                    urlsMapList.add(urlsMap);
                }
            } else {//没有","，即只有一个url
                String[] pictureUrls = new String[]{pictureUrl};
                Commodities commodities = new Commodities(commodity, pictureUrls);
                commoditiesList.add(commodities);
                //用另外一个存储图片的url，这里加一个url即可
                HashMap<String, Object> urlsMap = new HashMap<String, Object>();
                urlsMap.put("urlId", 0);
                urlsMap.put("urlSrc", pictureUrls[0]);
                urlsMapList.add(urlsMap);
            }
            //封装每个商品的id以及对应的url(s)
            pictureMap.put("cmId", buy.getCmId());
            pictureMap.put("picUrls", urlsMapList);
            pictureMapList.add(pictureMap);

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
        map.put("pictureList", pictureMapList);
        map.put("orderList", orderMapList);
        map.put("errorCode", 0);
        return map;
    }
}