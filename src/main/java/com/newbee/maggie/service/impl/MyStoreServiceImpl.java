package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.Commodities;
import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.mapper.CommodityMapper;
import com.newbee.maggie.service.MyStoreService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyStoreServiceImpl implements MyStoreService {
    @Autowired
    private CommodityMapper commodityMapper;

    private final Logger logger = Logger.getLogger(MyStoreService.class);

    // 外网地址
    private static final String SERVER_ADDRESS = "http://maggiemarket.design:8081";
    // 本地地址
    private static final String LOCAL_ADDRESS = "C:/xampp/tomcat/webapps";

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
     * 分割url，并删除服务器上的图片
     * @param pictureUrl
     * @return
     * @throws FileNotFoundException
     */
    private Map<String, Integer> deleteUrl(String pictureUrl) throws FileNotFoundException {
        logger.info("执行删除服务器上的图片操作");
        String[] pictureUrls;
        if (pictureUrl.contains(",")) {//如果有","，即不止一个url
            pictureUrls = pictureUrl.split(",");
        } else {//没有","，即只有一个url
            pictureUrls = new String[]{pictureUrl};
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (pictureUrls.length == 0) {
            logger.info("该商品无图片可删除");
            map.put("countToDelete", 0);
            return map;
        }
        int successCount = 0;
        int failureCount = 0;
        for (String url: pictureUrls) {
            String urlAddress = url.replaceFirst(SERVER_ADDRESS, LOCAL_ADDRESS);
            System.out.println(urlAddress);
            File file = new File(urlAddress);
            // 判断文件是否存在
            if (file.isFile() && file.exists()) {
                if (file.delete()) {
                    logger.info("删除" + urlAddress + "成功");
                    successCount++;
                } else {
                    logger.info("删除" + urlAddress + "失败");
                    failureCount++;
                }
            } else {
                logger.info("删除" + urlAddress + "时图片不存在");
            }
        }
        map.put("successCount", successCount);
        map.put("failureCount", failureCount);
        logger.info("该商品的图片删除情况如上");
        return map;
    }

    @Override
    public List<Commodity> getCmListByUserId(Integer userId) {
        return commodityMapper.getCmListByUserId(userId);
    }

    @Override
    public List<Commodities> getCmsListByUserId(Integer userId) {
        List<Commodity> commodityList = commodityMapper.getCmListByUserId(userId);
        return splitUrl(commodityList);
    }

    @Override
    public Integer getUserIdByCmId(Integer cmId) {
        return commodityMapper.getUserIdByCmId(cmId);
    }

    @Transactional
    @Override
    public Boolean addCommodity(Commodity commodity) {
        //先尝试插入commodity表
        Integer count = commodityMapper.getCmCount();
        Integer cmId = new Integer(0);
        if (count == 0) {
            cmId = 1;
        } else {
            cmId = commodityMapper.getMaxId() + 1;
        }
        commodity.setCmId(cmId);//设置cmId
        System.out.println("commodity: " + commodity);
        try {
            int effectedNum = commodityMapper.insertCommodity(commodity);
            if (effectedNum > 0) {
                //插入成功
                return true;
            } else {
                throw new RuntimeException("插入商品失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("插入商品失败" + e.toString());
        }
    }

    @Transactional
    @Override
    public Boolean updateCommodity(Commodity commodity) {
        // 空值判断，主要是判断name不为空
        if (commodity.getName() != null && commodity.getName().trim().length() != 0 && !commodity.getName().equals("")) {
            try {
                // 先保存url的信息
                Integer cmId = commodity.getCmId();
                Commodity commodityOld = commodityMapper.getCommodityByCmId(cmId);
                String pictureUrl = commodityOld.getPictureUrl();
                // 更新商品信息
                int effectedNum = commodityMapper.updateCommodity(commodity);
                if (effectedNum > 0) {
                    // 更新成功了就把服务器上的旧的图片一起删除
                    // 这里先不删了
                    //Map<String, Integer> deleteMap = deleteUrl(pictureUrl);
                    //logger.info("删除情况：" + deleteMap);
                    return true;
                } else {
                    throw new RuntimeException("更新商品信息失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("更新商品信息失败:" + e.toString());
            }
        } else {
            throw new RuntimeException("商品名不能为空");
        }
    }

    @Transactional
    @Override
    public Boolean deleteCommodity(Integer cmId) {
        // 判断cmId是否为空
        if (cmId > 0) {
            try {
                // 先保存url的信息
                Commodity commodity = commodityMapper.getCommodityByCmId(cmId);
                String pictureUrl = commodity.getPictureUrl();
                // 删除商品信息
                int effectedNum = commodityMapper.deleteCmByCmId(cmId);
                if (effectedNum > 0) {
                    // 删除成功了就把服务器上的图片一起删除
                    Map<String, Integer> deleteMap = deleteUrl(pictureUrl);
                    logger.info("删除情况：" + deleteMap);
                    return true;
                } else {
                    throw new RuntimeException("删除商品信息失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("删除商品信息失败:" + e.toString());
            }
        } else {
            throw new RuntimeException("cmId不能为空");
        }
    }
}
