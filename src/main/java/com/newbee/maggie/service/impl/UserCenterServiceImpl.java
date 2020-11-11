package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.mapper.*;
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

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ReserveMapper reserveMapper;

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

    @Override
    public List<Collect> getCollectByUserId(Integer userId) {
        return collectMapper.findCollectByUserId(userId);
    }

    @Override
    public List<Reserve> getReserveByUserId(Integer userId) {
        return reserveMapper.findReserveByUserId(userId);
    }
}
