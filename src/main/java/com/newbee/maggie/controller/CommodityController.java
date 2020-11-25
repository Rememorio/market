package com.newbee.maggie.controller;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.service.CommodityService;
import com.newbee.maggie.util.CommodityNotFoundException;
import com.newbee.maggie.util.ParamNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    private final Logger logger = Logger.getLogger(CommodityController.class);

    /**
     * 根据商品id返回商品信息
     * @param idMap cmId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     * @throws CommodityNotFoundException 商品不存在
     */
    @RequestMapping(value = "/information", method = RequestMethod.POST)
    private Map<String, Object> commodityInfo(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException, CommodityNotFoundException {
        logger.info("==========执行请求商品详情==========");
        logger.info("请求体为：" + idMap);
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Integer userIdReq = idMap.get("userId");
        if (userIdReq == null) {
            throw new ParamNotFoundException("userId参数为空");
        } else if (userIdReq == -1) {
            logger.info("该用户未登录，正在请求cmId = " + cmId + "的商品详情");
        } else {
            logger.info("userId = " + userIdReq + "正在请求cmId = " + cmId + "的商品详情");
        }
        Map<String, Object> map = new HashMap<>();
        Commodity commodity = commodityService.getCommodityByCmId(cmId);
        if (commodity == null) {
            throw new CommodityNotFoundException("商品不存在");
        }
        //商品状态：int,1-待审核，2-审核通过，3-审核不通过，4-被预定 ，5-已付款 售出，6-被举报
        int state = commodity.getState();
        //是否被预定
        if (state == 4) {
            Reserve reserve = commodityService.getReserveByCmId(cmId);
            if (reserve == null) {
                map.put("reserved", false);
                map.put("content", "奇怪了，商品被预订了但是没有这个预定记录");
            } else {
                Integer reserveUserId = reserve.getUserId();
                if (reserveUserId == null) {
                    map.put("reserved", false);
                    map.put("content", "见鬼了，有这个预订记录但是找不到这个用户");
                } else {
                    String reserveInfo = commodityService.getContactInfoByUserId(reserveUserId);
                    map.put("reserved", true);
                    map.put("reserveTime", reserve.getReserveTime());
                    map.put("reserveId", reserve.getReserveId());
                    map.put("reservoirInfo", reserveInfo);
                }
            }
        } else {
            map.put("reserved", false);
        }
        //是否售出
        if (state == 5) {
            Buy buy = commodityService.getBuyByCmId(cmId);
            if (buy == null) {
                map.put("sold", false);
                map.put("content", "奇怪了，商品被购买了但是没有这个购买记录");
            } else {
                Integer buyUserId = buy.getUserId();
                if (buyUserId == null) {
                    map.put("sold", false);
                    map.put("content", "见鬼了，有这个购买记录但是找不到这个用户");
                } else {
                    String buyInfo = commodityService.getContactInfoByUserId(buyUserId);
                    map.put("sold", true);
                    map.put("timeOfReserve", buy.getTimeOfReserve());
                    map.put("timeOfTransaction", buy.getTimeOfTransaction());
                    map.put("orderId", buy.getOrderId());
                    map.put("buyerInfo", buyInfo);
                }
            }
        } else {
            map.put("sold", false);
        }
        //是否被收藏
        Integer userId = commodity.getUserId();
        String contactInfo = commodityService.getContactInfoByUserId(userId);
        if (userIdReq != -1) {
            boolean collected = commodityService.getIsCollected(userIdReq, cmId);
            map.put("collected", collected);
        } else {
            map.put("collected", false);
        }
        map.put("contactInfo", contactInfo);


        map.put("errorCode", 0);
        //处理图片url，以","作为分隔符
        String pictureUrl = commodity.getPictureUrl();
        if (pictureUrl.contains(",")) {//如果有","，即不止一个url
            String[] pictureUrls = pictureUrl.split(",");
            Commodities commodities = new Commodities(commodity, pictureUrls);
            map.put("commodityInfo", commodities);
            List<HashMap<String, Object>> urlsMapList = new ArrayList<>();
            for (int i = 0; i < pictureUrls.length; i++) {//逐一添加url
                HashMap<String, Object> urlsMap = new HashMap<>();
                urlsMap.put("urlId", i);
                urlsMap.put("urlSrc", pictureUrls[i]);
                urlsMapList.add(urlsMap);
            }
            map.put("urlList", urlsMapList);
        } else {//没有","，即只有一个url
            String[] pictureUrls = new String[]{pictureUrl};
            Commodities commodities = new Commodities(commodity, pictureUrls);
            map.put("commodityInfo", commodities);
            List<HashMap<String, Object>> urlsMapList = new ArrayList<>();
            HashMap<String, Object> urlsMap = new HashMap<>();
            urlsMap.put("urlId", 0);//这里添加一个url就可以了
            urlsMap.put("urlSrc", pictureUrls[0]);
            urlsMapList.add(urlsMap);
            map.put("urlList", urlsMapList);
        }
        logger.info("返回信息：" + map);
        logger.info("————————————————————");
        return map;
    }

    /**
     * 用户收藏商品
     * @param idMap userId, cmId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    private Map<String, Object> commodityCollection(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        logger.info("==========执行收藏请求==========");
        logger.info("请求体为：" + idMap);
        Integer userId = idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        logger.info("userId = " + userId + "正在收藏cmId = " + cmId + "的商品");
        Collect collect = new Collect(userId, cmId);
        Map<String, Object> map = new HashMap<>();
        if (commodityService.addCollection(collect)) {
            map.put("errorCode", 0);
            map.put("success", true);
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 用户取消收藏商品
     * @param idMap userId, cmId
     * @return mep
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/collectionCancel", method = RequestMethod.POST)
    private Map<String, Object> commodityCollectionCancel(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        logger.info("==========执行取消收藏请求==========");
        logger.info("请求体为：" + idMap);
        Integer userId = idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        logger.info("userId = " + userId + "正在取消收藏cmId = " + cmId + "的商品");
        Collect collect = new Collect(userId, cmId);
        Map<String, Object> map = new HashMap<>();
        if (commodityService.deleteCollection(collect)) {
            map.put("errorCode", 0);
            map.put("success", true);
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 用户预定商品
     * @param idMap cmId, userId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/reserve", method = RequestMethod.POST)
    private Map<String, Object> commodityReserve(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        logger.info("==========执行预订请求==========");
        logger.info("请求体为：" + idMap);
        Integer userId = idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        logger.info("userId = " + userId + "正在预订cmId = " + cmId + "的商品");
        Reserve reserve = new Reserve(cmId, userId);
        Map<String, Object> map = new HashMap<>();
        if (commodityService.addReserve(reserve)) {
            map.put("errorCode", 0);
            map.put("success", true);
            map.put("reserveId", reserve.getReserveId());
            map.put("reserveTime", reserve.getReserveTime());
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 用户取消预订商品
     * @param idMap cmId, userId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/reserveCancel", method = RequestMethod.POST)
    private Map<String, Object> commodityReserveCancel(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        logger.info("==========执行取消预订请求==========");
        logger.info("请求体为：" + idMap);
        Integer userId = idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        logger.info("userId = " + userId + "正在取消预订cmId = " + cmId + "的商品");
        Map<String, Object> map = new HashMap<>();
        if (commodityService.deleteReserve(cmId)) {
            map.put("errorCode", 0);
            map.put("success", true);
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 用户购买商品
     * @param idMap cmId, userId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    private Map<String, Object> commodityBuy(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        logger.info("==========执行购买请求==========");
        logger.info("请求体为：" + idMap);
        Integer userId = idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        logger.info("userId = " + userId + "正在购买cmId = " + cmId + "的商品");
        Buy buy = new Buy(cmId, userId);
        Map<String, Object> map = new HashMap<>();
        if (commodityService.addBuy(buy)) {
            map.put("errorCode", 0);
            map.put("success", true);
            map.put("orderId", buy.getOrderId());
            map.put("timeOfTransaction", buy.getTimeOfTransaction());
            map.put("timeOfReserve", buy.getTimeOfReserve());
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 用户删除订单信息
     * @param idMap userId, cmId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/buyDelete", method = RequestMethod.POST)
    private Map<String, Object> commodityBuyDelete(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        logger.info("==========执行删除购买请求==========");
        logger.info("请求体为：" + idMap);
        Integer userId = idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        logger.info("userId = " + userId + "正在删除购买cmId = " + cmId + "的商品");
        Map<String, Object> map = new HashMap<>();
        if (commodityService.deleteBuy(cmId)) {
            map.put("errorCode", 0);
            map.put("success", true);
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }

    /**
     * 用户举报商品
     * @param idMap userId, cmId
     * @return map
     * @throws ParamNotFoundException 参数缺失
     */
    @RequestMapping(value = "/accuse", method = RequestMethod.POST)
    private Map<String, Object> commodityReport(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        logger.info("==========执行举报请求==========");
        logger.info("请求体为：" + idMap);
        Integer userId = idMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Integer cmId = idMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        logger.info("userId = " + userId + "正在举报cmId = " + cmId + "的商品");
        Map<String, Object> map = new HashMap<>();
        if (commodityService.reportCommodity(cmId)) {
            map.put("errorCode", 0);
            map.put("success", true);
            logger.info("返回信息：" + map);
            logger.info("————————————————————");
            return map;
        }
        //理论上运行不到这里
        logger.info("太阳从西边出来了");
        return map;
    }
}
