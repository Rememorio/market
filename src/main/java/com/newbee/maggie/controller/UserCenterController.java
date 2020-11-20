package com.newbee.maggie.controller;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.service.UserCenterService;
import com.newbee.maggie.util.*;
import com.newbee.maggie.web.ResponseVO;
import com.newbee.maggie.web.UserInfoVO;
import com.newbee.maggie.web.WxLoginVO;
import com.newbee.maggie.web.Year;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/userCenter")
public class UserCenterController {
    @Autowired
    private UserCenterService userCenterService;

    private Logger logger = Logger.getLogger(UserCenterController.class);

    /**
     * 根据用户名返回用户信息
     * @param usernameMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfoByNickname", method = RequestMethod.POST)
    private Map<String, Object> userInfoByUserName(@RequestBody Map<String, String> usernameMap) throws ParamNotFoundException, UserNotFoundException {
        logger.info("执行请求根据用户名查找用户详情");
        String username = usernameMap.get("nickname");//获取昵称
        if (username == null || username.length() == 0) {//如果没有这个昵称
            throw new ParamNotFoundException("nickname参数为空");
        }
        logger.info("正在请求nickname = " + username + "的用户信息");
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByNickname(username);
        if(user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("errorCode", 0);
        map.put("userInfo", user);
        logger.info("返回信息：" + map);
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
        logger.info("执行请求根据用户id查找用户详情");
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("正在请求userId = " + userId + "的用户信息");
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("errorCode", 0);
        map.put("userInfo", user);
        logger.info("返回信息：" + map);
        return map;
    }

    /**
     * 登录授权
     * @param codeMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Map<String, String> codeMap) throws Exception {
        logger.info("执行授权登录请求");
        String encryptedData = codeMap.get("encryptedData");
        if (encryptedData == null) {
            throw new ParamNotFoundException("encryptedData参数为空");
        }
        String iv = codeMap.get("iv");
        if (iv == null) {
            throw new ParamNotFoundException("iv参数为空");
        }
        String rawData = codeMap.get("rawData");
        if (rawData == null) {
            throw new ParamNotFoundException("rawData参数为空");
        }
        String signature = codeMap.get("signature");
        if (signature == null) {
            throw new ParamNotFoundException("signature参数为空");
        }
        String code = codeMap.get("code");
        if (code == null) {
            throw new ParamNotFoundException("code参数为空");
        }
        logger.info("请求参数：" + codeMap);
        WxLoginVO loginVO = new WxLoginVO(encryptedData, iv, rawData, signature, code);
        ResponseVO<UserInfoVO> responseVO = userCenterService.login(loginVO);
        logger.info("返回信息："  + responseVO);
        UserInfoVO userInfoVO =  responseVO.getData();
        Map<String, Object> map = new HashMap<String, Object>();
        // 如果没有获取用户信息，就把错误码报上去
        if (userInfoVO == null) {
            map.put("errorCode", 1);
            if (responseVO.getCode() == MsgError.COMMON_EMPTY.code()) {
                map.put("errorMsg", MsgError.COMMON_EMPTY.getErrorMsg());
            }
            if (responseVO.getCode() == MsgError.WX_SIGNATURE.code()) {
                map.put("errorMsg", MsgError.WX_SIGNATURE.getErrorMsg());
            }
            logger.info("授权失败："  + map);
            return map;
        }
        // 如果获取用户信息，就返回用户id
        map.put("errorCode", 0);
        map.put("userId", userInfoVO.getUserId());
        logger.info("授权成功："  + map);
        return map;
    }

    /**
     * 根据用户id查询是否具有管理员权限
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/getIsAdmin", method = RequestMethod.POST)
    private Map<String, Object> getIsAdmin(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException {
        logger.info("执行请求根据用户id查看管理员权限");
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在请求查看管理员权限");
        //查找用户
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        int authority = user.getAuthority();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("authority", authority);
        logger.info("返回信息：" + map);
        return map;
    }

    /**
     * 获取年级数组
     * @return
     */
    @RequestMapping(value = "/userInfo/gradeArray", method = RequestMethod.GET)
    private Map<String, Object> gradeArray() {
        logger.info("执行请求年级数组");
        Year year = new Year();
        //封装信息
        Map<String, Object> map = new HashMap<String, Object>();
        if (year.yearChanged() == 0) {
            map.put("errorCode", 0);
            map.put("gradeArray", Year.year);
        } else if (year.yearChanged() == 1) {
            map.put("errorCode", 0);
            map.put("gradeArray", year.getYears());
        } else {
            map.put("errorCode", 1);
        }
        logger.info("返回信息：" + map);
        return map;
    }

    /**
     * 获取可修改的用户信息
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfo/userInfo", method = RequestMethod.POST)
    private Map<String, Object> userInfo(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException {
        logger.info("执行请求待修改的用户信息");
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在请求待修改的用户信息");
        //查找用户
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        //计算年级数组下标
        Year year = new Year();
        Integer gradeIndex = year.getIndex(year.yearChanged(), user.getGrade());
        //封装信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", user.getNickname());
        map.put("grade", user.getGrade());
        map.put("contactInfo", user.getContactInformation());
        map.put("addressInfo", user.getDefaultShippingAddress());
        map.put("gradeIndex", gradeIndex);
        map.put("errorCode", 0);
        logger.info("返回信息：" + map);
        return map;
    }

    /**
     * 修改用户联系方式、收货地址和年级
     * @param userInfoMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfo/infoUpdate", method = RequestMethod.POST)
    private Map<String, Object> userInfoUpdated(@RequestBody Map<String, Object> userInfoMap) throws ParamNotFoundException, UserNotFoundException {
        logger.info("执行请求修改联系方式、收货地址和年级");
        Integer userId = (Integer) userInfoMap.get("userId");
        Integer gradeIndex = (Integer) userInfoMap.get("gradeIndex");
        String contactInfo = (String) userInfoMap.get("contactInfo");
        String addressInfo = (String) userInfoMap.get("addressInfo");
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        if (gradeIndex == null) {
            throw new ParamNotFoundException("gradeIndex参数为空");
        }
        if (contactInfo == null) {
            throw new ParamNotFoundException("contactInfo参数为空");
        }
        if (addressInfo == null) {
            throw new ParamNotFoundException("addressInfo参数为空");
        }
        logger.info("userId = " + userId + "正在请求修改联系方式、收货地址和年级");
        //查找用户
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        //计算年份
        Year year = new Year();
        Integer grade = year.getYear(year.yearChanged(), gradeIndex);
        //更新信息
        user.setContactInformation(contactInfo);
        user.setDefaultShippingAddress(addressInfo);
        user.setGrade(grade);
        //更新数据库
        Map<String, Object> map = new HashMap<String, Object>();
        if (userCenterService.updateUser(user)) {//如果成功
            map.put("errorCode", 0);
            map.put("success", true);
        }
        logger.info("返回信息：" + map);
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
        logger.info("执行请求我买到的商品列表");
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在请求我买到的商品列表");
        //查找用户
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
        logger.info("返回信息：" + map);
        return map;
    }

    /**
     * 根据用户id查找用户收藏
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     * @throws CommodityNotFoundException
     */
    @RequestMapping(value = "/userFavors", method = RequestMethod.POST)
    private Map<String, Object> userFavors(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException, CommodityNotFoundException {
        logger.info("执行请求我的收藏商品列表");
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在请求我的收藏商品列表");
        //查找用户
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
        logger.info("返回信息：" + map);
        return map;
    }

    /**
     * 根据用户id查找用户预订
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     * @throws UserNotFoundException
     * @throws CommodityNotFoundException
     */
    @RequestMapping(value = "/userBookings", method = RequestMethod.POST)
    private Map<String, Object> userBookings(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException, CommodityNotFoundException {
        logger.info("执行请求我预订的商品列表");
        Integer userId = userIdMap.get("userId");//获取用户id
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        logger.info("userId = " + userId + "正在请求我预订的商品列表");
        //查找用户
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
        logger.info("返回信息：" + map);
        return map;
    }
}