package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.mapper.UserMapper;
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

    /**
     * 如果关键词里面有地址，那么把这部分的也加上
     * 这个算法有问题，如果我搜【课本 大学城】，会返回所有课本和所有大学城的商品
     * 再说吧，看什么时候改了
     * @param originList
     * @param searchInput
     * @return
     */
    private List<Commodity> addAdress(List<Commodity> originList, String searchInput) {
        List<Commodity> commodityList = new ArrayList<>();
        if (searchInput.contains("大学城")) {
            List<Commodity> hemc = commodityMapper.getHigherEducationMegaCenterCmList();
            commodityList.removeAll(hemc);//去重
            commodityList.addAll(hemc);//再合并
        }
        if (searchInput.contains("五山")) {
            List<Commodity> ws = commodityMapper.getWuShanCmList();
            commodityList.removeAll(ws);
            commodityList.addAll(ws);
        }
        if (searchInput.contains("国际")) {
            List<Commodity> ic = commodityMapper.getInternationalCmList();
            commodityList.removeAll(ic);
            commodityList.addAll(ic);
        }
        originList.removeAll(commodityList);
        originList.addAll(commodityList);
        return originList;
    }

    @Override
    public List<Commodity> getRecommendedCommodity() {
        Integer cmCount = commodityMapper.getApprovedCmCount();
        if (cmCount < 10) {//如果商品数少于10那就直接返回那么多，有可能是空集
            return commodityMapper.getApprovedCmList();
        } else {//如果大于10
            List<Commodity> allCmList = commodityMapper.getApprovedCmList();//先获取全部的商品
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
        return splitUrl(commodityList);
    }

    @Override
    public List<User> searchUser(String searchInput) {
        String keyword = "%" + searchInput + "%";
        return userMapper.getUserListBySearching(keyword);
    }

    @Override
    public List<Commodity> searchCommodity(String searchInput) {
        String keyword = "%" + searchInput + "%";
        return addAdress(commodityMapper.getCmListBySearching(keyword), searchInput);
    }

    @Override
    public List<Commodities> searchCommodities(String searchInput) {
        List<Commodity> commodityList = searchCommodity(searchInput);//直接套用上面的函数
        return splitUrl(commodityList);
    }

    @Override
    public List<Commodity> searchCommodityPriceUp(String searchInput) {
        String keyword = "%" + searchInput + "%";
        return addAdress(commodityMapper.getCmListBySearchingPriceUp(keyword), searchInput);
    }

    @Override
    public List<Commodities> searchCommoditiesPriceUp(String searchInput) {
        List<Commodity> commodityList = searchCommodityPriceUp(searchInput);
        return splitUrl(commodityList);
    }

    @Override
    public List<Commodity> searchCommodityPriceDown(String searchInput) {
        String keyword = "%" + searchInput + "%";
        return addAdress(commodityMapper.getCmListBySearchingPriceDown(keyword), searchInput);
    }

    @Override
    public List<Commodities> searchCommoditiesPriceDown(String searchInput) {
        List<Commodity> commodityList = searchCommodityPriceDown(searchInput);
        return splitUrl(commodityList);
    }

    @Override
    public List<Commodity> searchCommodityTimeNew(String searchInput) {
        String keyword = "%" + searchInput + "%";
        return addAdress(commodityMapper.getCmListBySearchingTimeNew(keyword), searchInput);
    }

    @Override
    public List<Commodities> searchCommoditiesTimeNew(String searchInput) {
        List<Commodity> commodityList = searchCommodityTimeNew(searchInput);
        return splitUrl(commodityList);
    }

    @Override
    public List<Commodity> searchCommodityTimeOld(String searchInput) {
        String keyword = "%" + searchInput + "%";
        return addAdress(commodityMapper.getCmListBySearchingTimeOld(keyword), searchInput);
    }

    @Override
    public List<Commodities> searchCommoditiesTimeOld(String searchInput) {
        List<Commodity> commodityList = searchCommodityTimeOld(searchInput);
        return splitUrl(commodityList);
    }

}
