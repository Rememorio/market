package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.service.HomeService;
import com.newbee.maggie.util.ParamIllegalException;
import com.newbee.maggie.util.ParamNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    private Logger logger = Logger.getLogger(HomeController.class);

    /**
     * 推荐商品
     * @return
     */
    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    private Map<String, Object> recommendation() {
        logger.info("执行请求推荐商品列表");
        List<Commodities> cmsList = new ArrayList<Commodities>();
        cmsList = homeService.getRecommendedCommodities();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("commodityList", cmsList);
        logger.info("返回信息：" + map);
        return map;
    }

    /**
     * 搜索商品或者用户
     * @param searchMap
     * @return
     * @throws ParamNotFoundException
     * @throws ParamIllegalException
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    private Map<String, Object> search(@RequestBody Map<String, Object> searchMap) throws ParamNotFoundException, ParamIllegalException {
        logger.info("执行请求搜索用户或商品");
        String searchInput = (String) searchMap.get("searchInput");
        if (searchInput == null) {
            throw new ParamNotFoundException("searchInput参数为空");
        }
        //这里的searchType会从int和string之间横跳，需要判断类型
        Object searchTypeTemp = searchMap.get("searchType");
        if (searchTypeTemp == null) {
            throw new ParamNotFoundException("searchType参数为空");
        }
        Integer searchType = new Integer(-1);
        if (searchTypeTemp instanceof Integer) {
            searchType = (Integer) searchTypeTemp;
        } else if (searchTypeTemp instanceof String) {
            try {
                searchType = Integer.valueOf((String) searchTypeTemp);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("searchType参数不合法");
            }
        } else {
            throw new ParamIllegalException("searchType参数不合法");
        }
        //识别类型之后开始判断是商品还是用户
        Map<String, Object> map = new HashMap<String, Object>();
        if (searchType == 0) {//商品
            //这里的排序方式也会反复横跳
            Object sortTypeTemp = searchMap.get("sortType");
            if (sortTypeTemp == null) {
                throw new ParamNotFoundException("sortType参数为空");
            }
            Integer sortType = new Integer(-1);
            if (sortTypeTemp instanceof Integer) {
                sortType = (Integer) sortTypeTemp;
            } else if (searchTypeTemp instanceof String) {
                try {
                    sortType = Integer.valueOf((String) sortTypeTemp);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("sortType参数不合法");
                }
            } else {
                throw new ParamIllegalException("sortType参数不合法");
            }
            //识别类型之后开始判断是价格升序还是时间最新
            List<Commodities> commoditiesList;
            if (sortType == 0) {//价格升序
                logger.info("搜索了商品，价格升序，关键词为：" + searchInput);
                commoditiesList = homeService.searchCommoditiesPriceUp(searchInput);
            } else if (sortType == 1) {
                logger.info("搜索了商品，时间最新，关键词为：" + searchInput);
                commoditiesList = homeService.searchCommoditiesTimeNew(searchInput);
            } else {
                throw new ParamIllegalException("sortType参数不合法");
            }
            map.put("errorCode", 0);
            map.put("CommodityList", commoditiesList);
            logger.info("返回信息：" + map);
            return map;
        } else if (searchType == 1) {//用户
            logger.info("搜索了用户，关键词为：" + searchInput);
            List<User> userList = homeService.searchUser(searchInput);
            map.put("errorCode", 0);
            map.put("UserList", userList);
            logger.info("返回信息：" + map);
            return map;
        } else {
            throw new ParamIllegalException("searchType参数不合法");
        }
    }
}
