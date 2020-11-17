package com.newbee.maggie.controller;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.service.CommodityService;
import com.newbee.maggie.util.CommodityNotFoundException;
import com.newbee.maggie.util.ParamNotFoundException;
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

    /**
     * 根据商品id返回商品信息
     * @param cmIdMap
     * @return
     * @throws CommodityNotFoundException
     */
    @RequestMapping(value = "/information", method = RequestMethod.POST)
    private Map<String, Object> commodityInfo(@RequestBody Map<String, Integer> cmIdMap) throws ParamNotFoundException, CommodityNotFoundException {
        Integer cmId = cmIdMap.get("cmId");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Commodity commodity = commodityService.getCommodityByCmId(cmId);
        if (commodity == null) {
            throw new CommodityNotFoundException("商品不存在");
        }

        Integer userId = commodity.getUserId();
        String contactInfo = commodityService.getContactInfoByUserId(userId);
        map.put("contactInfo", contactInfo);
        map.put("errorCode", 0);
        //处理图片url，以","作为分隔符
        String pictureUrl = commodity.getPictureUrl();
        if (pictureUrl.contains(",")) {//如果有","，即不止一个url
            String[] pictureUrls = pictureUrl.split(",");
            Commodities commodities = new Commodities(commodity, pictureUrls);
            map.put("commodityList", commodities);
            List<HashMap<String, Object>> urlsMapList = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < pictureUrls.length; i++) {//逐一添加url
                HashMap<String, Object> urlsMap = new HashMap<String, Object>();
                urlsMap.put("urlId", i);
                urlsMap.put("urlSrc", pictureUrls[i]);
                urlsMapList.add(urlsMap);
            }
            map.put("urlList", urlsMapList);
        } else {//没有","，即只有一个url
            String[] pictureUrls = new String[]{pictureUrl};
            Commodities commodities = new Commodities(commodity, pictureUrls);
            map.put("commodityInfo", commodities);
            List<HashMap<String, Object>> urlsMapList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> urlsMap = new HashMap<String, Object>();
            urlsMap.put("urlId", 0);//这里添加一个url就可以了
            urlsMap.put("urlSrc", pictureUrls[0]);
            urlsMapList.add(urlsMap);
            map.put("urlList", urlsMapList);
        }
        return map;
    }

    /**
     * 用户收藏商品
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    private Map<String, Object> commodityCollection(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        Integer userId = idMap.get("userId");
        Integer cmId = idMap.get("cmId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Collect collect = new Collect(userId, cmId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (commodityService.addCollection(collect)) {
            map.put("errorCode", 0);
            map.put("success", true);
            return map;
        }
        //能运行到这里肯定有问题
        map.put("errorCode", 1);
        return map;
    }

    /**
     * 用户取消收藏商品
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/collectionCancel", method = RequestMethod.POST)
    private Map<String, Object> commodityCollectionCancel(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        Integer userId = idMap.get("userId");
        Integer cmId = idMap.get("cmId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Collect collect = new Collect(userId, cmId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (commodityService.deleteCollection(collect)) {
            map.put("errorCode", 0);
            map.put("success", true);
            return map;
        }
        //能运行到这里肯定有问题
        map.put("errorCode", 1);
        return map;
    }

    /**
     * 用户收藏商品
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/reserve", method = RequestMethod.POST)
    private Map<String, Object> commodityReserve(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        Integer userId = idMap.get("userId");
        Integer cmId = idMap.get("cmId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Reserve reserve = new Reserve(cmId, userId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (commodityService.addReserve(reserve)) {
            map.put("errorCode", 0);
            map.put("success", true);
            map.put("reserveId", reserve.getReserveId());
            map.put("reserveTime", reserve.getReserveTime());
            return map;
        }
        //能运行到这里肯定有问题
        map.put("errorCode", 1);
        return map;
    }

    /**
     * 用户取消预订商品
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/reserveCancel", method = RequestMethod.POST)
    private Map<String, Object> commodityReserveCancel(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        Integer userId = idMap.get("userId");
        Integer cmId = idMap.get("cmId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (commodityService.deleteReserve(cmId)) {
            map.put("errorCode", 0);
            map.put("success", true);
            return map;
        }
        //能运行到这里肯定有问题
        map.put("errorCode", 1);
        return map;
    }

    /**
     * 用户购买商品
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    private Map<String, Object> commodityBuy(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        Integer userId = idMap.get("userId");
        Integer cmId = idMap.get("cmId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Buy buy = new Buy(cmId, userId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (commodityService.addBuy(buy)) {
            map.put("errorCode", 0);
            map.put("success", true);
            map.put("orderId", buy.getOrderId());
            map.put("timeOfTransaction", buy.getTimeOfTransaction());
            return map;
        }
        //能运行到这里肯定有问题
        map.put("errorCode", 1);
        return map;
    }

    /**
     * 用户删除订单信息
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/buyDelete", method = RequestMethod.POST)
    private Map<String, Object> commodityBuyDelete(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        Integer userId = idMap.get("userId");
        Integer cmId = idMap.get("cmId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (commodityService.deleteBuy(cmId)) {
            map.put("errorCode", 0);
            map.put("success", true);
            return map;
        }
        //能运行到这里肯定有问题
        map.put("errorCode", 1);
        return map;
    }

    /**
     * 用户举报商品
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/accuse", method = RequestMethod.POST)
    private Map<String, Object> commodityReport(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException {
        Integer userId = idMap.get("userId");
        Integer cmId = idMap.get("cmId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (commodityService.reportCommodity(cmId)) {
            map.put("errorCode", 0);
            map.put("success", true);
            return map;
        }
        //能运行到这里肯定有问题
        map.put("errorCode", 1);
        return map;
    }
}
