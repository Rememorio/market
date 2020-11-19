package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.User;

import java.util.List;

public interface UserMapper {

    /**
     * 根据用户名查找用户
     * @param nickname
     * @return
     */
    User getUserByUserName(String nickname);

    /**
     * 根据用户id查找用户
     * @param userId
     * @return
     */
    User getUserByUserId(Integer userId);

    /**
     * 获取用户数，方便生成用户id
     * @return
     */
    Integer getIdCount();

    /**
     * 添加新用户
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 根据用户id删除用户，不出意外这个函数应该不会被调用
     * @param userId
     * @return
     */
    int deleteUserByUserId(Integer userId);

    /**
     * 根据关键词查找用户
     * @param keyword
     * @return
     */
    List<User> getUserListBySearching(String keyword);

    /**
     * 根据userId查找用户联系方式
     * @param userId
     * @return
     */
    String getContactInfoByUserId(Integer userId);
}
