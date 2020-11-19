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

    /**
     * 根据商品id查找用户id，判断是不是这个卖家
     * @param cmId
     * @return
     */
    public Integer getUserIdByCmId(Integer cmId);

    /**
     * 添加商品
     * @param commodity
     * @return
     */
    public Boolean addCommodity(Commodity commodity);

    /**
     * 修改商品
     * @param commodity
     * @return
     */
    public Boolean updateCommodity(Commodity commodity);

    /**
     * 删除商品
     * @param cmId
     * @return
     */
    public Boolean deleteCommodity(Integer cmId);
}
