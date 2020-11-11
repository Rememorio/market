package com.newbee.maggie.service;

import com.newbee.maggie.entity.Buy;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;

import java.util.List;

public interface UserCenterService {

    /**
     * 通过昵称查找用户
     * @param nickname
     * @return User
     */
    public User getUserByNickname(String nickname);

    /**
     * 通过用户id查找用户
     * @param userId
     * @return User
     */
    public User getUserByUserId(Integer userId);

    /**
     * 通过商品id查找商品
     * @param cmId
     * @return
     */
    public Commodity getCommodityByCmId(Integer cmId);

    /**
     * 通过用户id查找已购列表
     * @param userId
     * @return
     */
    public List<Buy> getBuyByUserId(Integer userId);
}
