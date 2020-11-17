package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Buy;

import java.util.List;

public interface BuyMapper {
    /**
     * 根据用户id查找用户已买列表
     * @param userId
     * @return
     */
    List<Buy> getBuyByUserId(Integer userId);

    /**
     * 根据商品id查找订单信息
     * @param cmId
     * @return
     */
    List<Buy> getBuyByCmId(Integer cmId);

    /**
     * 获取Buy表元组数，方便生成orderId
     * @return
     */
    Integer getIdCount();

    /**
     * 插入订单信息
     * @param buy
     * @return
     */
    int insertBuy(Buy buy);

    /**
     * 删除订单信息
     * @param cmId
     * @return
     */
    int deleteBuy(Integer cmId);
}
