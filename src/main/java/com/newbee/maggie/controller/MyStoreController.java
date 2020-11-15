package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.service.MyStoreService;
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
@RequestMapping("/myStore")
public class MyStoreController {
    @Autowired
    private MyStoreService myStoreService;

    /**
     * 根据userId获取这个用户出售的商品
     * @param userIdMap
     * @return
     * @throws ParamNotFoundException
     */
    @RequestMapping(value = "/getMyGoods", method = RequestMethod.POST)
    private Map<String, Object> myGoods(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException {
        Integer userId = userIdMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        List<Commodity> cmList = new ArrayList<Commodity>();
        cmList = myStoreService.getCmListBuyUserId(userId);
        //封装信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", 0);
        map.put("commodityList", cmList);
        return map;
    }
}
