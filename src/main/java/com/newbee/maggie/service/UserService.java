package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodity;

public interface UserService {
    /**
     * 通过id查找商品
     * @param cmId
     * @return
     */
    public Commodity getCommodityByCmId(Integer cmId);

}
