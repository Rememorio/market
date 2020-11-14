package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.User;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 获取用户数，方便生成用户id
     * @return
     */
    Integer getIdCount();

    /**
     * 根据openid寻找userid，判断数据库有没有这个用户
     * @return
     */
    Integer findUserIdByOpenId(String openId);

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
}
