package com.newbee.maggie.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newbee.maggie.controller.UserCenterController;
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

    private Logger logger = Logger.getLogger(UserCenterService.class);

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
    public boolean addUser(User user) {
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
    public boolean updateUser(User user) {
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
    public boolean deleteUserByUserId(Integer userId) {
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
    public ResponseVO<UserInfoVO> login(WxLoginVO loginVO) throws Exception {
        logger.info("正在执行code2session");
        String code2SessionUrl = WxUtils.getApi(WxConsts.API_SESSION_KEY, loginVO.getCode());
        logger.info("请求url：" + code2SessionUrl);
        String result = HttpUtils.doGet(code2SessionUrl);
        logger.info("返回参数：" + result);
        WxAuthVO wxAuthVO = JSONObject.parseObject(result, WxAuthVO.class);
        logger.info("openid&sessionKey: " + wxAuthVO);
        //如果签名不一致
        if(!loginVO.getSignature().equals(WxUtils.getSignature(loginVO.getRawData(),wxAuthVO.getSessionKey()))){
            return new ResponseVO<UserInfoVO>(MsgError.WX_SIGNATURE.code(), MsgError.WX_SIGNATURE.getErrorMsg(),null);
        }
        //如果成功获取到openid
        if (wxAuthVO.getOpenId() != null) {
            logger.info("成功获取openId：" + wxAuthVO.getOpenId());
            UserInfo userinfo = null;
            UserInfoVO respUserInfo = null;
            userinfo = userInfoMapper.selectByOpenId(wxAuthVO.getOpenId());
            //如果是新用户，那么就操作数据库
            if(userinfo == null){
                logger.info("该用户为新用户，userInfo：" + userinfo);
                WxUserInfoVO wxUserInfo = WxUtils.encryptedDataUserInfo(loginVO.getEncryptedData(), wxAuthVO.getSessionKey(), loginVO.getIv());
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
                    int effectedNumber = userMapper.insertUser(user);
                    if (effectedNumber > 0) {
                        logger.info("插入user表失败，userId：" + userId);
                    } else {
                        logger.info("插入user表失败，userId：" + userId);
                    }
                } else {
                    logger.info("插入user_info表成功，userId：" + userId);
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
}
