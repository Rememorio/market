package com.newbee.maggie.service;

import com.newbee.maggie.entity.*;

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
     * 获取用户数，以便于生成用户id
     * @return
     */
    public Integer getUserCount();

    /**
     * 插入新用户
     * @param user
     * @return
     */
    public Integer addUser(User user);

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

    /**
     * 通过用户id查找收藏的商品
     * @param userId
     * @return
     */
    public List<Collect> getCollectByUserId(Integer userId);

    /**
     * 通过用户id查找预定的商品
     * @param userId
     * @return
     */
    public List<Reserve> getReserveByUserId(Integer userId);
}
