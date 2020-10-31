package com.newbee.maggie.controller;

import com.newbee.maggie.entity.User;
import com.newbee.maggie.service.UserService;
import com.newbee.maggie.util.UserNotFoundException;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.management.ObjectInstance;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userCenter")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    private Map<String, Object> userInfo(@Param("nickname") String username) throws UserNotFoundException {
        //System.out.println("username: " + username);
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userService.getUserByNickname(username);
        if(user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("data", user);
        return map;
    }

    @RequestMapping(value = "/userInfo/userId", method = RequestMethod.GET)
    private  Map<String, Object> userInfo(@Param("userId") Integer userId) throws UserNotFoundException {
        //System.out.println("userId: " + userId);
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userService.getUserByUserId(userId);
        if(user == null) {//如果没有这个用户，就抛出用户不存在的异常
            throw new UserNotFoundException("用户不存在");
        }
        map.put("data", user);
        return map;
    }
    /**
     * 个人中心-授权
     */
//    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
//    private Map<String, Object> authorize(boolean hasUserInfo) {
//
//    }
    /**
     * 个人中心-退出账号
     */
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    private Map<String, Object> logout(String code) {
//
//    }

    /**
     * 用户权限查询
     */
//    @RequestMapping(value = "/isAdministrator", method = RequestMethod.GET)
//    private Map<String, Object> isAdministrator(int userId) {
//
//    }
    /**
     * 个人资料-信息修改
     */
//    @RequestMapping(value = "/userInfo/editFinish", method = RequestMethod.GET)
//    private Map<String, Object> editFinish(StringArray gradeArray, int gradeIndex, boolean change, boolean finish, User userinfo) {
//
//    }
    /**
     * 我的收藏
     */
//    @RequestMapping(value = "/userFavors", method = RequestMethod.GET)
//    private Map<String, Object> userFavors(String searchInput) {
//
//    }
}