package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Buy;

import java.util.List;

public interface BuyMapper {
    /**
     * 根据用户id查找用户已买列表
     * @param userId
     * @return
     */
    List<Buy> findBuyByUserId(Integer userId);
}
