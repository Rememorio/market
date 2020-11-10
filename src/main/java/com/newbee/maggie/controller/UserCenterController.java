package com.newbee.maggie.controller;

import com.newbee.maggie.entity.Commodity;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.service.UserCenterService;
import com.newbee.maggie.util.CommodityNotFoundException;
import com.newbee.maggie.util.ParamNotFoundException;
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
     * @param usernameMap
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfoByNickname", method = RequestMethod.POST)
    private Map<String, Object> userInfoWithUserName(@RequestBody Map<String, String> usernameMap) throws ParamNotFoundException, UserNotFoundException {
        String username = usernameMap.get("nickname");
        if (username == null) {
            throw new ParamNotFoundException("nickname参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByNickname(username);
        if(user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("errorCode", 0);
        map.put("data", user);
        return map;
    }

    /**
     * 根据用户id返回用户信息
     * @param userIdMap
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/userInfoByUserId", method = RequestMethod.POST)
    private  Map<String, Object> userInfoWithUserId(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException {
        Integer userId = userIdMap.get("userId");
        if (userId == null) {//如果没有userId信息
            throw new ParamNotFoundException("userId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("errorCode", 0);
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
     * @param userIdMap
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/getIsAdmin", method = RequestMethod.POST)
    private Map<String, Object> getIsAdmin(@RequestBody Map<String, Integer> userIdMap) throws ParamNotFoundException, UserNotFoundException {
        Integer userId = userIdMap.get("userId");
        if (userId == null) {
            throw new ParamNotFoundException("userId参数为空");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userCenterService.getUserByUserId(userId);
        if (user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        int authority = user.getAuthority();
        Map<String, Integer> auth = new HashMap<String, Integer>();//封装用户权限信息
        auth.put("authority", authority);
        map.put("errorCode", 0);
        map.put("data", auth);
        return map;
    }
}