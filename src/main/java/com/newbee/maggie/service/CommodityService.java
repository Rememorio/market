package com.newbee.maggie.service;

import com.newbee.maggie.entity.*;

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

    /**
     * 通过用户id查找用户联系方式
     * @param userId
     * @return
     */
    public String getContactInfoByUserId(Integer userId);

    /**
     * 收藏商品
     * @param collect
     * @return
     */
    public Boolean addCollection(Collect collect);

    /**
     * 取消收藏商品
     * @param collect
     * @return
     */
    public Boolean deleteCollection(Collect collect);

    /**
     * 预订商品
     * @param reserve
     * @return
     */
    public Boolean addReserve(Reserve reserve);

    /**
     * 取消预定商品
     * @param cmId
     * @return
     */
    public Boolean deleteReserve(Integer cmId);

    /**
     * 购买商品
     * @param buy
     * @return
     */
    public Boolean addBuy(Buy buy);

    /**
     * 删除购买信息（这个函数应该不会被用到叭）
     * @param cmId
     * @return
     */
    public Boolean deleteBuy(Integer cmId);

    /**
     * 举报商品
     * @param cmId
     * @return
     */
    public Boolean reportCommodity(Integer cmId);
}
