package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.User;

public interface UserMapper {

    /**
     * 根据用户名查找用户
     * @param nickname
     * @return
     */
    User findUserByUsername(String nickname);

    /**
     * 添加新用户
     * @param user
     * @return
     */
    int insertUser(User user);
}
