package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;

import java.util.List;

public interface HomeService {
    /**
     * 返回推荐的商品
     * @return
     */
    public List<Commodity> getRecommendedCommodity();

    /**
     * 返回推荐的商品，并把url进行分割
     * @return
     */
    public List<Commodities> getRecommendedCommodities();

    /**
     * 搜索用户
     * @param searchInput
     * @return
     */
    public List<User> searchUser(String searchInput);

    /**
     * 搜索商品
     * @param searchInput
     * @return
     */
    public List<Commodity> searchCommodity(String searchInput);

    /**
     * 搜索商品，并把url进行分割
     * @param searchInput
     * @return
     */
    public List<Commodities> searchCommodities(String searchInput);

    /**
     * 搜索商品，价格升序
     * @param searchInput
     * @return
     */
    public List<Commodity> searchCommodityPriceUp(String searchInput);

    /**
     * 搜索商品，价格升序，并把url进行分割
     * @param searchInput
     * @return
     */
    public List<Commodities> searchCommoditiesPriceUp(String searchInput);

    /**
     * 搜索商品，价格降序
     * @param searchInput
     * @return
     */
    public List<Commodity> searchCommodityPriceDown(String searchInput);

    /**
     * 搜索商品，价格降序，并把url进行分割
     * @param searchInput
     * @return
     */
    public List<Commodities> searchCommoditiesPriceDown(String searchInput);

    /**
     * 搜索商品，时间最新
     * @param searchInput
     * @return
     */
    public List<Commodity> searchCommodityTimeNew(String searchInput);

    /**
     * 搜索商品，时间最新，并把url进行分割
     * @param searchInput
     * @return
     */
    public List<Commodities> searchCommoditiesTimeNew(String searchInput);

    /**
     * 搜索商品，时间最久
     * @param searchInput
     * @return
     */
    public List<Commodity> searchCommodityTimeOld(String searchInput);

    /**
     * 搜索商品，时间最久，并把url进行分割
     * @param searchInput
     * @return
     */
    public List<Commodities> searchCommoditiesTimeOld(String searchInput);
}
