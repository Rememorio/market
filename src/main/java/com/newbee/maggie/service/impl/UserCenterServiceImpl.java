package com.newbee.maggie.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newbee.maggie.entity.*;
import com.newbee.maggie.mapper.*;
import com.newbee.maggie.service.UserCenterService;
import com.newbee.maggie.util.*;
import com.newbee.maggie.web.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserCenterServiceImpl implements UserCenterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private BuyMapper buyMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ReserveMapper reserveMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    private final Logger logger = Logger.getLogger(UserCenterService.class);

    @Override
    public User getUserByNickname(String nickname) {
        return userMapper.getUserByUserName(nickname);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.getUserByUserId(userId);
    }

    @Override
    public Integer getUserCount() {
        return userMapper.getIdCount();
    }

    @Transactional  //加上事务控制  当抛出RuntimeException异常  事务就会回滚
    @Override
    public Boolean addUser(User user) {
        // 判定数据库里面有没有这个人
        User userTemp = userMapper.getUserByUserId(user.getUserId());
        if (userTemp != null)
            throw new RuntimeException("数据库中已经有该用户，添加新用户失败");
        // 空值判断，主要是判断userName不为空
        if (user.getNickname() != null && user.getNickname().trim().length() != 0 && !user.getNickname().equals("")){
            try {
                int effectedNum = userMapper.insertUser(user);
                if (effectedNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("添加新用户失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("添加新用户失败:" + e.toString());
            }
        } else {
            throw new RuntimeException("用户信息不能为空");
        }
    }

    @Transactional
    @Override
    public Boolean updateUser(User user) {
        // 空值判断，主要是判断userName不为空
        if (user.getNickname() != null && user.getNickname().trim().length() != 0 && !user.getNickname().equals("")) {
            try {
                // 更新用户信息
                int effectedNum = userMapper.updateUser(user);
                if (effectedNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("更新用户信息失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("更新用户信息失败:" + e.toString());
            }
        } else {
            throw new RuntimeException("用户信息不能为空");
        }
    }

    @Transactional
    @Override
    public Boolean deleteUserByUserId(Integer userId) {
        // 判断用户id是否为空
        if (userId > 0) {
            try {
                // 删除用户信息
                int effectedNum = userMapper.deleteUserByUserId(userId);
                if (effectedNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("删除用户信息失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("删除用户信息失败:" + e.toString());
            }
        } else {
            throw new RuntimeException("userId不能为空");
        }
    }

    @Override
    public Commodity getCommodityByCmId(Integer cmId) {
        return commodityMapper.getCommodityByCmId(cmId);
    }

    @Override
    public Commodities getCommoditiesByCmId(Integer cmId) {
        Commodity commodity = commodityMapper.getCommodityByCmId(cmId);
        Commodities commodities = null;
        if (commodity != null) {
            String pictureUrl = commodity.getPictureUrl();
            System.out.println(pictureUrl);
            if (pictureUrl.contains(",")) {//如果有","，即不止一个url
                String[] pictureUrls = pictureUrl.split(",");
                commodities = new Commodities(commodity, pictureUrls);
            } else {//没有","，即只有一个url
                String[] pictureUrls = new String[]{pictureUrl};
                commodities = new Commodities(commodity, pictureUrls);
            }
        }
        return commodities;
    }

    @Override
    public List<Buy> getBuyByUserId(Integer userId) {
        return buyMapper.getBuyByUserId(userId);
    }

    @Override
    public List<Collect> getCollectByUserId(Integer userId) {
        return collectMapper.getCollectByUserId(userId);
    }

    @Override
    public List<Reserve> getReserveByUserId(Integer userId) {
        return reserveMapper.getReserveByUserId(userId);
    }

    @Override
    public Reserve getReserveByCmId(Integer cmId) {
        return reserveMapper.getReserveByCmId(cmId);
    }

    @Override
    public ResponseVO<UserInfoVO> login(WxLoginVO loginVO) throws Exception {
        logger.info("正在执行code2session");
        String code2SessionUrl = WxUtils.getApi(WxConsts.API_SESSION_KEY, loginVO.getCode());
        logger.info("请求url：" + code2SessionUrl);
        String result = HttpUtils.doGet(code2SessionUrl);
        logger.info("返回参数：" + result);
        WxAuthVO wxAuthVO = JSONObject.parseObject(result, WxAuthVO.class);
        logger.info("openid&sessionKey: " + wxAuthVO);
        logger.info("登录的signature：" + loginVO.getSignature());
        logger.info("校验的signature：" + WxUtils.getSignature(loginVO.getRawData(),wxAuthVO.getSession_key()));
        //如果签名不一致
//        if(!loginVO.getSignature().equals(WxUtils.getSignature(loginVO.getRawData(),wxAuthVO.getSession_key()))){
//            logger.info("微信签名不一致");
//            return new ResponseVO<UserInfoVO>(MsgError.WX_SIGNATURE.code(), MsgError.WX_SIGNATURE.getErrorMsg(),null);
//        }
        //如果成功获取到openid
        if (wxAuthVO.getOpenid() != null) {
            logger.info("成功获取openId：" + wxAuthVO.getOpenid());
            UserInfo userinfo = null;
            UserInfoVO respUserInfo = null;
            userinfo = userInfoMapper.selectByOpenId(wxAuthVO.getOpenid());
            //如果是新用户，那么就操作数据库
            if(userinfo == null){
                logger.info("该用户为新用户，userInfo：" + userinfo);
                WxUserInfoVO wxUserInfo = WxUtils.encryptedDataUserInfo(loginVO.getEncryptedData(), wxAuthVO.getSession_key(), loginVO.getIv());
                userinfo = JSON.parseObject(JSON.toJSONString(wxUserInfo),UserInfo.class);
                //设置默认参数
                Integer userId = userInfoMapper.getIdCount() + 1;
                userinfo.setUserId(userId);//这里可能还要设置一下userId
                userinfo.setUsertype("1");
                //生成user对象，准备插入user表
                String nickname = userinfo.getNickname();
                User user = new User(nickname);
                user.setUserId(userId);
                //开始插入user_info表
                int effectedNum = userInfoMapper.insert(userinfo);
                if (effectedNum > 0) {//如果插入成功
                    logger.info("插入user_info表成功，userId：" + userId);
                    //继续插入user表
                    if (addUser(user)) {
                        logger.info("插入user表成功，userId：" + userId);
                    } else {
                        logger.info("插入user表失败，userId：" + userId);
                    }
                } else {
                    logger.info("插入user_info表失败，userId：" + userId);
                }
            } else {
                logger.info("该用户不是新用户，userInfo：" + userinfo);
            }
            respUserInfo = JSON.parseObject(JSON.toJSONString(userinfo),UserInfoVO.class);
            respUserInfo.setToken(TokenUtil.getToken(userinfo));

            return new ResponseVO<UserInfoVO>(MsgSuccess.OK.code(), MsgSuccess.OK.getSuccesMsg(), respUserInfo);
        }
        //获取不到openid
        return new ResponseVO<UserInfoVO>(MsgError.COMMON_EMPTY.code(), MsgError.COMMON_EMPTY.getErrorMsg(),null);
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
        Integer cmId = buy.getCmId();
        Integer orderId = reserveMapper.getReserveIdByCmId(cmId);
        if (orderId == null) {
            throw new RuntimeException("可能是该商品还没有被预订，无法购买");
        }
        String timeOfReserve = reserveMapper.getReserveTimeByCmId(cmId);
        if (timeOfReserve == null) {
            throw new RuntimeException("该商品没有预订时间信息，无法购买");
        }
        buy.setOrderId(orderId);//设置orderId
        buy.setTimeOfReserve(timeOfReserve);//设置预订时间
        try {
            int effectedNum = buyMapper.insertBuy(buy);
            if (effectedNum > 0) {
                //更改商品状态为已售出
                try {
                    int effectedNumber = commodityMapper.changeStateToSold(cmId);
                    if (effectedNumber > 0) {
                        //最后更新reserve中的元组
                        try {
                            int effectedNumbers = reserveMapper.updateReserve(cmId);
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
}
