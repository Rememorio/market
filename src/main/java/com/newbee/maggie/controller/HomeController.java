package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 推荐商品
     * @return
     */
    @RequestMapping(value = "recommend", method = RequestMethod.GET)
    private Map<String, Object> recommendation() {
        List<Commodity> cmList = new ArrayList<>();
        cmList = homeService.getRecommendedCommodity();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("commodityList", cmList);
        return map;
    }
}
