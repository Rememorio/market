package com.newbee.maggie.service;

import com.newbee.maggie.entity.User;

public interface UserService {

    /**
     * 通过昵称查找用户
     * @param nickname
     * @return
     */
    public User getUserByNickname(String nickname);

}
