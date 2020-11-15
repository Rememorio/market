package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Override
    public Commodity getCommodityByCmId(Integer cmId) {
        return commodityMapper.getCommodityByCmId(cmId);
    }

    @Override
    public Commodities getCommoditiesByCmId(Integer cmId) {
        Commodity commodity = commodityMapper.getCommodityByCmId(cmId);
        Commodities commodities;
        String pictureUrl = commodity.getPictureUrl();
        if (pictureUrl.contains(",")) {//如果有","，即不止一个url
            String[] pictureUrls = pictureUrl.split(",");
            commodities = new Commodities(commodity, pictureUrls);
        } else {//没有","，即只有一个url
            String[] pictureUrls = new String[]{pictureUrl};
            commodities = new Commodities(commodity, pictureUrls);
        }
        return commodities;
    }
}
