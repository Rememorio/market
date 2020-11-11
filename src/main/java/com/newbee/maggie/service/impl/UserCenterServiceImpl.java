package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Buy;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.mapper.BuyMapper;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.mapper.UserMapper;
import com.newbee.maggie.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCenterServiceImpl implements UserCenterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private BuyMapper buyMapper;

    @Override
    public User getUserByNickname(String nickname) {
        return userMapper.findUserByUsername(nickname);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.findUserByUserId(userId);
    }

    @Override
    public Commodity getCommodityByCmId(Integer cmId) {
        return commodityMapper.findCommodityByCmId(cmId);
    }

    @Override
    public List<Buy> getBuyByUserId(Integer userId) {
        return buyMapper.findBuyByUserId(userId);
    }
}
