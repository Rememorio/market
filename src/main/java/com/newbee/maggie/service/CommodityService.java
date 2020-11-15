package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;

public interface CommodityService {
    /**
     * 通过id查找商品
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
}
