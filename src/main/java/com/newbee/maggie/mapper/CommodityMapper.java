package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Commodity;

import java.util.List;

public interface CommodityMapper {

    /**
     * 根据商品id查找商品
     * @param cmId
     * @return
     */
    Commodity getCommodityByCmId(Integer cmId);

    /**
     * 根据用户id查找该用户出售的所有商品
     * @param userId
     * @return
     */
    List<Commodity> getCmListByUserId(Integer userId);
}
