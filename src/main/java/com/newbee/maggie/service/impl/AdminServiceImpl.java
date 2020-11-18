package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.mapper.UserMapper;
import com.newbee.maggie.service.AdminService;
import com.newbee.maggie.util.ParamIllegalException;
import com.newbee.maggie.util.ParamNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 分割url
     * @param commodityList
     * @return
     */
    private List<Commodities> splitUrl(List<Commodity> commodityList) {
        List<Commodities> commoditiesList = new ArrayList<Commodities>();
        for (Commodity commodity: commodityList) {
            Commodities commodities;
            String pictureUrl = commodity.getPictureUrl();
            if (pictureUrl.contains(",")) {//如果有","，即不止一个url
                String[] pictureUrls = pictureUrl.split(",");
                commodities = new Commodities(commodity, pictureUrls);
            } else {//没有","，即只有一个url
                String[] pictureUrls = new String[]{pictureUrl};
                commodities = new Commodities(commodity, pictureUrls);
            }
            commoditiesList.add(commodities);
        }
        return commoditiesList;
    }

    @Override
    public List<Commodity> getReportedCommodity() {
        return commodityMapper.getReportedCmList();
    }

    @Override
    public List<Commodities> getReportedCommodities() {
        List<Commodity> commodityList = commodityMapper.getReportedCmList();
        return splitUrl(commodityList);
    }

    @Override
    public List<Commodity> getWaitingCommodity() {
        return commodityMapper.getWaitingCmList();
    }

    @Override
    public List<Commodities> getWaitingCommodities() {
        List<Commodity> commodityList = commodityMapper.getWaitingCmList();
        return splitUrl(commodityList);
    }

    @Override
    public Commodity getCommodityByCmId(Integer cmId) {
        return commodityMapper.getCommodityByCmId(cmId);
    }

    @Override
    public Commodities getCommoditiesByCmId(Integer cmId) {
        Commodity commodity = commodityMapper.getCommodityByCmId(cmId);
        Commodities commodities;
        String pictureUrl = commodity.getPictureUrl();
        if (pictureUrl.contains(",")) {//如果有","，即不止一个url
            String[] pictureUrls = pictureUrl.split(",");
            commodities = new Commodities(commodity, pictureUrls);
        } else {//没有","，即只有一个url
            String[] pictureUrls = new String[]{pictureUrl};
            commodities = new Commodities(commodity, pictureUrls);
        }
        return commodities;
    }

    @Override
    public String getContactInfoByUserId(Integer userId) {
        return userMapper.getContactInfoByUserId(userId);
    }

    @Transactional
    @Override
    public int changeState(Integer cmId, int toState){
        if (toState == 2) {//审核通过
            //更改商品状态为审核通过
            try {
                int effectedNumber = commodityMapper.changeStateToApproved(cmId);
                if (effectedNumber > 0) {
                    return 2;
                } else {
                    throw new RuntimeException("更改状态至审核通过失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("更改状态至审核通过失败：" + e.toString());
            }
        } else if (toState == 3) {//审核不通过
            //更改商品状态为审核不通过
            try {
                int effectedNumber = commodityMapper.changeStateToNotApproved(cmId);
                if (effectedNumber > 0) {
                    return 3;
                } else {
                    throw new RuntimeException("更改状态至审核通过失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("更改状态至审核通过失败：" + e.toString());
            }
        } else {
            return -1;
        }
    }
}
