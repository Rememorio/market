package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.service.CommodityService;
import com.newbee.maggie.util.CommodityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    private Map<String, Object> commodityInfo(@RequestBody Map<String, Integer> cmIdMap) throws CommodityNotFoundException {
        Integer cmId = cmIdMap.get("cm_id");
        Map<String, Object> map = new HashMap<String, Object>();
        Commodity commodity = commodityService.getCommodityByCmId(cmId);
        if (commodity == null) {
            throw new CommodityNotFoundException("商品不存在");
        }
        map.put("error_200", 0);
        map.put("data", commodity);
        return map;
    }
}
