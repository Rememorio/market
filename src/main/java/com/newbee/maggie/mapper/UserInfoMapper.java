package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.UserInfo;

public interface UserInfoMapper {
    /**
     * 根据openid返回微信用户信息
     * @param openId
     * @return
     */
    UserInfo selectByOpenId(String openId);

    /**
     * 插入微信用户信息
     * @param userInfo
     * @return
     */
    int insert(UserInfo userInfo);

    /**
     * 获取用户数，方便生成用户id
     * @return
     */
    Integer getIdCount();
}
