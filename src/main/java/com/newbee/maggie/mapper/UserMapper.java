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
     * 根据用户id查找用户
     * @param userId
     * @return
     */
    User findUserByUserId(Integer userId);

//    //以下在xml还没实现
//    /**
//     * 添加新用户
//     * @param user
//     * @return
//     */
//    int insertUser(User user);
//
//    /**
//     * 修改用户信息
//     * @param user
//     * @return
//     */
//    int updateUser(User user);
//
//    /**
//     * 根据用户id删除用户
//     * @param userId
//     * @return
//     */
//    int deleteUserByUserId(Integer userId);
}
