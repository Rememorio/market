package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.*;
import com.newbee.maggie.mapper.*;
import com.newbee.maggie.service.CommodityService;
import com.newbee.maggie.util.CommodityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ReserveMapper reserveMapper;

    @Autowired
    private BuyMapper buyMapper;

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

    @Override
    public boolean getIsCollected(Integer userId, Integer cmId) {
        Collect collect = new Collect(userId, cmId);
        Collect collectTarget = collectMapper.getCollectByUserIdAndCmId(collect);
        if (collectTarget == null) {
            return false;
        }
        return true;
    }

    @Transactional  //加上事务控制  当抛出RuntimeException异常  事务就会回滚
    @Override
    public Boolean addCollection(Collect collect) {
        try {
            int effectedNum = collectMapper.insertCollect(collect);
            if (effectedNum > 0) {
                return true;
            } else {
                throw new RuntimeException("收藏失败，可能是用户已经收藏该商品");
            }
        } catch (Exception e) {
            throw new RuntimeException("收藏失败，可能是用户已经收藏该商品:" + e.toString());
        }
    }

    @Transactional
    @Override
    public Boolean deleteCollection(Collect collect) {
        try {
            //删除收藏
            int effectedNum = collectMapper.deleteCollect(collect);
            if (effectedNum > 0) {
                return true;
            } else {
                throw new RuntimeException("取消收藏失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("取消收藏失败:" + e.toString());
        }
    }

    @Transactional
    @Override
    public Boolean addReserve(Reserve reserve) {
        Commodity commodity = commodityMapper.getCommodityByCmId(reserve.getCmId());
        if (commodity.getState() != 2) {
            throw new RuntimeException("该商品状态不为审核通过，无法预订");
        }
        //先尝试插入reserve表
        Integer count = reserveMapper.getIdCount();
        Integer reserveId = new Integer(0);
        if (count == 0) {
            reserveId = 1;
        } else {
            reserveId = reserveMapper.getMaxId() + 1;
        }
        reserve.setReserveId(reserveId);//设置reserveId
        try {
            int effectedNum = reserveMapper.insertReserve(reserve);
            if (effectedNum > 0) {
                //更改商品状态为被预定
                Integer cmId = reserve.getCmId();
                try {
                    int effectedNumber = commodityMapper.changeStateToReserved(cmId);
                    if (effectedNumber > 0) {
                        return true;
                    } else {
                        throw new RuntimeException("更改状态至被预定失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("更改状态至被预定失败：" + e.toString());
                }
            } else {
                throw new RuntimeException("预定失败，可能是其他用户已经预定该商品");
            }
        } catch (Exception e) {
            throw new RuntimeException("预定失败，可能是其他用户已经预定该商品:" + e.toString());
        }
    }

    @Transactional
    @Override
    public Boolean deleteReserve(Integer cmId) {
        //先试图删除reserve中的元组
        try {
            int effectedNum = reserveMapper.deleteReserve(cmId);
            if (effectedNum > 0) {
                //更改商品状态为审核通过
                try {
                    int effectedNumber = commodityMapper.changeStateToApproved(cmId);
                    if (effectedNumber > 0) {
                        return true;
                    } else {
                        throw new RuntimeException("更改状态至审核通过失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("更改状态至审核通过失败：" + e.toString());
                }
            } else {
                throw new RuntimeException("取消预定失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("取消预定失败:" + e.toString());
        }
    }

    @Transactional
    @Override
    public Boolean addBuy(Buy buy) {
        Commodity commodity = commodityMapper.getCommodityByCmId(buy.getCmId());
        if (commodity == null) {
            throw new RuntimeException("商品不存在，无法购买");
        }
        if (!(commodity.getState() == 2 || commodity.getState() == 4)) {
            throw new RuntimeException("该商品状态不为审核通过或已预订，无法购买");
        }
        Reserve reserve = reserveMapper.getReserveByCmId(commodity.getCmId());
        if (!reserve.getUserId().equals(buy.getUserId())) {
            throw new RuntimeException("该用户不是该商品的预订者，无法购买");
        }
        //先试图插入buy表
        Integer orderId = reserveMapper.getReserveIdByCmId(buy.getCmId());
        if (orderId == null) {
            throw new RuntimeException("可能是该商品还没有被预定，无法购买");
        }
        buy.setOrderId(orderId);//设置orderId
        try {
            int effectedNum = buyMapper.insertBuy(buy);
            if (effectedNum > 0) {
                //更改商品状态为已售出
                Integer cmId = buy.getCmId();
                try {
                    int effectedNumber = commodityMapper.changeStateToSold(cmId);
                    if (effectedNumber > 0) {
                        //最后删除reserve中的元组
                        try {
                            int effectedNumbers = reserveMapper.deleteReserve(cmId);
                            if (effectedNumbers > 0) {
                                return true;
                            } else {
                                throw new RuntimeException("删除预定失败");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("删除预定失败:" + e.toString());
                        }
                    } else {
                        throw new RuntimeException("更改状态至已售出失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("更改状态至已售出失败：" + e.toString());
                }
            } else {
                throw new RuntimeException("购买失败，可能是该商品已经被购买");
            }
        } catch (Exception e) {
            throw new RuntimeException("购买失败，可能是该商品已经被购买:" + e.toString());
        }
    }

    @Transactional
    @Override
    public Boolean deleteBuy(Integer cmId) {
        //先试图删除buy中的元组
        try {
            int effectedNum = buyMapper.deleteBuy(cmId);
            if (effectedNum > 0) {
                //更改商品状态为null
                try {
                    int effectedNumber = commodityMapper.changeStateToNull(cmId);
                    if (effectedNumber > 0) {
                        return true;
                    } else {
                        throw new RuntimeException("更改状态至null失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("更改状态至null失败：" + e.toString());
                }
            } else {
                throw new RuntimeException("删除购买信息失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("删除购买信息失败:" + e.toString());
        }
    }

    @Transactional
    @Override
    public Boolean reportCommodity(Integer cmId) {
        //更改商品状态为被举报
        try {
            int effectedNumber = commodityMapper.changeStateToReported(cmId);
            if (effectedNumber > 0) {
                return true;
            } else {
                throw new RuntimeException("更改状态至被举报失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("更改状态至被举报失败：" + e.toString());
        }
    }
}
