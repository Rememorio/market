package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.service.AdminService;
import com.newbee.maggie.util.CommodityNotFoundException;
import com.newbee.maggie.util.ParamIllegalException;
import com.newbee.maggie.util.ParamNotFoundException;
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
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 获取被举报的商品
     * @return
     */
    @RequestMapping(value = "/accused", method = RequestMethod.GET)
    private Map<String, Object> reportedCommodity() {
        List<Commodities> cmsList = new ArrayList<Commodities>();
        cmsList = adminService.getReportedCommodities();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("commodityList", cmsList);
        return map;
    }

    /**
     * 获取待审核的商品
     * @return
     */
    @RequestMapping(value = "/waiting", method = RequestMethod.GET)
    private Map<String, Object> waitingCommodity() {
        List<Commodities> cmsList = new ArrayList<Commodities>();
        cmsList = adminService.getWaitingCommodities();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("commodityList", cmsList);
        return map;
    }

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
        Commodity commodity = adminService.getCommodityByCmId(cmId);
        if (commodity == null) {
            throw new CommodityNotFoundException("商品不存在");
        }

        Integer userId = commodity.getUserId();
        String contactInfo = adminService.getContactInfoByUserId(userId);
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
     * 审核商品，2-通过，3-不通过
     * @param idMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/changeState", method = RequestMethod.POST)
    private Map<String, Object> changeState(@RequestBody Map<String, Integer> idMap) throws ParamNotFoundException, ParamIllegalException {
        Integer cmId = idMap.get("cmId");
        Integer toState = idMap.get("toState");
        if (cmId == null) {
            throw new ParamNotFoundException("cmId参数为空");
        }
        if (toState == null) {
            throw new ParamNotFoundException("toState参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        //尝试更改状态
        if (toState == 2) {//审核通过
            if (adminService.changeState(cmId, 2) == 2) {
                map.put("errorCode", 0);
                map.put("cmId", cmId);
                map.put("state", 2);
                return map;
            }
        } else if (toState == 3) {//审核不通过
            if (adminService.changeState(cmId, 3) == 3) {
                map.put("errorCode", 0);
                map.put("cmId", cmId);
                map.put("state", 3);
                return map;
            }
        } else {
            throw new ParamIllegalException("toState参数不合法");
        }
        //估计运行不到这里
        map.put("errorCode", 1);
        return map;
    }
}
