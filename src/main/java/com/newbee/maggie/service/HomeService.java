package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodity;

import java.util.List;

public interface HomeService {
    /**
     * 返回推荐的商品
     * @return
     */
    public List<Commodity> getRecommendedCommodity();
}
