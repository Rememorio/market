package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCenterImpl implements UserService {
    @Autowired
    CommodityMapper commodityMapper;

    @Override
    public Commodity getCommodityByCmId(Integer cmId) {
        return commodityMapper.findCommodityByCmId(cmId);
    }
}
