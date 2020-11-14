package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.mapper.UserMapper;
import com.newbee.maggie.service.MyStoreService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MyStoreServiceImpl implements MyStoreService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Commodity> getCmListBuyUserId(Integer userId) {
        return commodityMapper.getCmListByUserId(userId);
    }
}
