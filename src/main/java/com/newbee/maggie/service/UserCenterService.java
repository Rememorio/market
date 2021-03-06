package com.newbee.maggie.service;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.web.ResponseVO;
import com.newbee.maggie.web.UserInfoVO;
import com.newbee.maggie.web.WxLoginVO;

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
     * 获取用户数
     * @return
     */
    public Integer getUserCount();

    /**
     * 插入新用户
     * @param user
     * @return
     */
    public Boolean addUser(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public Boolean updateUser(User user);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    public Boolean deleteUserByUserId(Integer userId);

    /**
     * 通过商品id查找商品
     * @param cmId
     * @return
     */
    public Commodity getCommodityByCmId(Integer cmId);

    /**
     * 通过id查找商品，并把url进行分割
     * @param cmId
     * @return
     */
    public Commodities getCommoditiesByCmId(Integer cmId);

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

    /**
     * 根据商品id查找预定信息
     * @param cmId
     * @return
     */
    public Reserve getReserveByCmId(Integer cmId);

    /**
     * 授权登录
     * @param loginVO
     * @return
     * @throws Exception
     */
    public ResponseVO<UserInfoVO> login(WxLoginVO loginVO) throws Exception;

    /**
     * 取消预定，删除reserve元组
     * @param cmId
     * @return
     */
    public Boolean deleteReserve(Integer cmId);

    /**
     * 购买成功，插入buy表
     * @param buy
     * @return
     */
    public Boolean addBuy(Buy buy);
}
