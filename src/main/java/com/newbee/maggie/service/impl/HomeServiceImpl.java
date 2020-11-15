package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Override
    public List<Commodity> getRecommendedCommodity() {
        Integer cmCount = commodityMapper.getCmCount();
        if (cmCount < 10) {//如果商品数少于10那就直接返回那么多，有可能是空集
            return commodityMapper.getCmList();
        } else {//如果大于10
            List<Commodity> allCmList = commodityMapper.getCmList();//先获取全部的商品
            List<Integer> cmListIndex = new ArrayList<Integer>();//生成所有下标
            for (int i=0; i<cmCount; i++) {
                cmListIndex.add(i);
            }
            Random rand = new Random();
            for ( int i = cmCount; i > 0; i-- ) {//打乱数组
                int randInd = rand.nextInt(i);//生成随机数交换数组
                Integer temp = cmListIndex.get(randInd);
                cmListIndex.set(randInd, cmListIndex.get(i-1));
                cmListIndex.set(i-1, temp);
            }
            List<Commodity> cmList = new ArrayList<Commodity>();
            for (int i=0; i<10; i++) {
                cmList.add(allCmList.get(i));
            }
            return cmList;
        }
    }

    @Override
    public List<Commodities> getRecommendedCommodities() {
        List<Commodity> commodityList = getRecommendedCommodity();//直接套用上面的函数
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
}
