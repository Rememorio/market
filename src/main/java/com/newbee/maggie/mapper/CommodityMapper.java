package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Commodity;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 将商品的信息改为已预订4
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定 ，5-已付款售出，6-被举报
     * @param cmId
     * @return
     */
    int changeStateToReserved(Integer cmId);

    /**
     * 将商品的信息改为审核通过2
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定 ，5-已付款售出，6-被举报
     * @param cmId
     * @return
     */
    int changeStateToApproved(Integer cmId);

    /**
     * 将商品的信息改为待审核1
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定 ，5-已付款售出，6-被举报
     * @param cmId
     * @return
     */
    int changeStateToWaiting(Integer cmId);

    /**
     * 将商品的信息改为审核不通过3
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定 ，5-已付款售出，6-被举报
     * @param cmId
     * @return
     */
    int changeStateToNotApproved(Integer cmId);

    /**
     * 将商品的信息改为已售出5
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定 ，5-已付款售出，6-被举报
     * @param cmId
     * @return
     */
    int changeStateToSold(Integer cmId);

    /**
     * 将商品的信息改为被举报6
     * 1-待审核，2-审核通过，3-审核不通过，4-被预定 ，5-已付款售出，6-被举报
     * @param cmId
     * @return
     */
    int changeStateToReported(Integer cmId);

    /**
     * 将商品的信息改为7，这个是请后台删除该商品
     * @param cmId
     * @return
     */
    int changeStateToNull(Integer cmId);
}
