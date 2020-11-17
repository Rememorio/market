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

    /**
     * 返回商品数
     * @return
     */
    Integer getCmCount();

    /**
     * 返回数据库内所有商品
     * @return
     */
    List<Commodity> getCmList();

    /**
     * 返回通过的商品数
     * @return
     */
    Integer getApprovedCmCount();

    /**
     * 返回所有通过的商品
     * @return
     */
    List<Commodity> getApprovedCmList();

    /**
     * 返回用户搜索的商品，默认排序
     * @param keyword
     * @return
     */
    List<Commodity> getCmListBySearching(String keyword);

    /**
     * 返回用户搜索的商品，价格升序
     * @param keyword
     * @return
     */
    List<Commodity> getCmListBySearchingPriceUp(String keyword);

    /**
     * 返回用户搜索的商品，价格降序
     * @param keyword
     * @return
     */
    List<Commodity> getCmListBySearchingPriceDown(String keyword);

    /**
     * 返回用户搜索的商品，时间最新
     * @param keyword
     * @return
     */
    List<Commodity> getCmListBySearchingTimeNew(String keyword);

    /**
     * 返回用户搜索的商品，时间最旧
     * @param keyword
     * @return
     */
    List<Commodity> getCmListBySearchingTimeOld(String keyword);
}
