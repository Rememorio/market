package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.mapper.UserMapper;
import com.newbee.maggie.service.MyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyStoreServiceImpl implements MyStoreService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Commodity> getCmListByUserId(Integer userId) {
        return commodityMapper.getCmListByUserId(userId);
    }

    @Override
    public List<Commodities> getCmsListByUserId(Integer userId) {
        List<Commodity> commodityList = commodityMapper.getCmListByUserId(userId);
        List<Commodities> commoditiesList = new ArrayList<Commodities>();
        for (Commodity commodity: commodityList) {
            Commodities commodities;
            String pictureUrl = commodity.getPictureUrl();
            if (pictureUrl.contains(",")) {//如果有","，即不止一个url
                String[] pictureUrls = pictureUrl.split(",");
                commodities = new Commodities(commodity, pictureUrls);
            } else {//没有","，即只有一个url
                String[] pictureUrls = new String[]{pictureUrl};
                commodities = new Commodities(commodity, pictureUrls);
            }
            commoditiesList.add(commodities);
        }
        return commoditiesList;
    }
}
