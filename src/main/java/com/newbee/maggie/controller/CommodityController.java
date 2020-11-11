package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
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
    @RequestMapping(value = "information", method = RequestMethod.POST)
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

        map.put("errorCode", 0);
        //处理图片url，以","作为分隔符
        String pictureUrl = commodity.getPictureUrl();
        if (pictureUrl.contains(",")) {//如果有","，即不止一个url
            String[] pictureUrls = pictureUrl.split(",");
            Commodities commodities = new Commodities(commodity, pictureUrls);
            map.put("data", commodities);
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
            map.put("data", commodities);
            List<HashMap<String, Object>> urlsMapList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> urlsMap = new HashMap<String, Object>();
            urlsMap.put("urlId", 0);//这里添加一个url就可以了
            urlsMap.put("urlSrc", pictureUrls[0]);
            urlsMapList.add(urlsMap);
            map.put("urlList", urlsMapList);
        }
        return map;
    }
}
