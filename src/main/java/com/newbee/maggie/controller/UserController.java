package com.newbee.maggie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.mapper.UserMapper;
import com.newbee.maggie.service.UserService;
import com.newbee.maggie.service.impl.UserServiceImpl;
import com.newbee.maggie.web.ApiResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    private User info(@Param("nickname") String username) {
        System.out.println("username: " + username);
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userService.getUserByNickname(username);
        return user;
    }



}