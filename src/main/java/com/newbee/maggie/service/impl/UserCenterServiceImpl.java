package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.mapper.*;
import com.newbee.maggie.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Integer getUserCount() {
        return userMapper.getIdCount();
    }

    @Transactional  //加上事务控制  当抛出RuntimeException异常  事务就会回滚
    @Override
    public Integer addUser(User user) {
        // 判定数据库里面有没有这个人
        Integer userIdTemp = userMapper.findUserIdByOpenId(user.getOpenId());
        if (userIdTemp != null)
            throw new RuntimeException("数据库中已经有该用户，添加新用户失败");
        // 空值判断，主要是判断userName不为空
        if (user.getNickname() != null && user.getNickname().trim().length() != 0 && !user.getNickname().equals("")){
            // 设置默认值
            Integer userId = userMapper.getIdCount() + 1;
            user.setUserId(userId);//生成用户id
            user.setContactInformation("请输入联系方式");//联系方式
            user.setDefaultShippingAddress("请输入收货地址");//默认地址置空
            user.setGrade(2018);//默认2018
            user.setAuthority(0);//默认没有管理员权限
            try {
                int effectedNum = userMapper.insertUser(user);
                if (effectedNum > 0) {
                    return userId;
                } else {
                    throw new RuntimeException("添加新用户失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("添加新用户失败:" + e.toString());
            }
        } else {
            throw new RuntimeException("用户信息不能为空");
        }
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
