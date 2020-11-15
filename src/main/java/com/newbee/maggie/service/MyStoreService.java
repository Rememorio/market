package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;

import java.util.List;

public interface MyStoreService {

    /**
     * 根据用户id查找这个用户出售的商品
     * @param userId
     * @return
     */
    public List<Commodity> getCmListByUserId(Integer userId);

    /**
     * 根据用户id查找这个用户出售的商品，并把url进行分割
     * @param userId
     * @return
     */
    public List<Commodities> getCmsListByUserId(Integer userId);
}
