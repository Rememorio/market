package com.newbee.maggie.service;

import com.newbee.maggie.entity.User;

public interface UserService {

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


}
