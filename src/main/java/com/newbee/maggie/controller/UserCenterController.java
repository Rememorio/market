package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.service.UserCenterService;
import com.newbee.maggie.util.CommodityNotFoundException;
import com.newbee.maggie.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userCenter")
public class UserCenterController {
    @Autowired
    private UserCenterService userCenterService;

    /**
     * 根据用户名返回用户信息
     * @param username
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    private Map<String, Object> userInfo(@RequestParam("nickname") String username) throws UserNotFoundException {
        //System.out.println("username: " + username);
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByNickname(username);
        if(user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("data", user);
        return map;
    }

    /**
     * 根据用户id返回用户信息
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfo/userId", method = RequestMethod.GET)
    private  Map<String, Object> userInfo(@RequestParam("userId") Integer userId) throws UserNotFoundException {
        //System.out.println("userId: " + userId);
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("data", user);
        return map;
    }

//    /**
//     * 个人中心-授权
//     */
//    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
//    private Map<String, Object> getUserInfo() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        return map;
//    }

    /**
     * 用户权限查询，是否具有管理员权限
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/getIsAdmin", method = RequestMethod.GET)
    private Map<String, Object> getIsAdmin(@RequestParam("userId") Integer userId) throws UserNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        int authority = user.getAuthority();
        Map<String, Object> auth = new HashMap<String, Object>();//封装用户权限信息
        auth.put("authority", authority);
        map.put("error_code", 0);
        map.put("data", auth);
        return map;
    }

    /**
     * 根据商品id返回商品信息
     * @param cmId
     * @return
     * @throws CommodityNotFoundException
     */
    @RequestMapping(value = "commodityInfo", method = RequestMethod.GET)
    private Map<String, Object> commodityInfo(@RequestParam("cmId") Integer cmId) throws CommodityNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        Commodity commodity = userCenterService.getCommodityByCmId(cmId);
        if (commodity == null) {
            throw new CommodityNotFoundException("商品不存在");
        }
        map.put("data", commodity);
        return map;
    }

//    @RequestMapping(value = "/userBookings", method = RequestMethod.GET)
//    private Map<String, Object> userBookings(@RequestParam("user_id") Integer userId) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        return map;
//    }
}