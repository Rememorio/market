package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Commodity;

public interface CommodityMapper {

    /**
     * 根据商品id查找商品
     * @param cmId
     * @return
     */
    Commodity findCommodityByCmId(Integer cmId);
}
