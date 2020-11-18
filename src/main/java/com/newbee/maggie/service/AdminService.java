package com.newbee.maggie.service;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;

import java.util.List;

public interface AdminService {
    /**
     * 获取被举报的商品列表
     * @return
     */
    public List<Commodity> getReportedCommodity();

    /**
     * 获取被举报的商品列表，并把url进行分割
     * @return
     */
    public List<Commodities> getReportedCommodities();

    /**
     * 获取待审核的商品列表
     * @return
     */
    public List<Commodity> getWaitingCommodity();

    /**
     * 获取待审核的商品列表，并把url进行分割
     * @return
     */
    public List<Commodities> getWaitingCommodities();

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
     * 更改商品状态
     * @param cmId
     * @return
     */
    public int changeState(Integer cmId, int toState);
}
