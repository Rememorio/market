package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;

public interface UserCenterService {

    /**
     * 通过昵称查找用户
     * @param nickname
     * @return User
     */
    public User getUserByNickname(String nickname);

    /**
     * 通过id查找用户
     * @param userId
     * @return User
     */
    public User getUserByUserId(Integer userId);

//    /**
//     * 进行授权登录操作
//     * @param userInfo
//     * @return User
//     */
//    public User getUserInfo(UserInfo userInfo);

    /**
     * 通过id查找商品
     * @param cmId
     * @return
     */
    public Commodity getCommodityByCmId(Integer cmId);
}
