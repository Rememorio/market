package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodity;

import java.util.List;

public interface MyStoreService {

    /**
     * 根据用户id查找这个用户出售的商品
     * @param userId
     * @return
     */
    public List<Commodity> getCmListBuyUserId(Integer userId);
}
