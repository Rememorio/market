package com.newbee.maggie;

import com.newbee.maggie.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Hello {

    @Autowired
    UserDao userDao;

    @RequestMapping("/login")
    public String index(String nickname, String contact_information) {
        int count = userDao.getUserByLoginName(nickname, contact_information);
        if (count>0)
            return "你是个傻逼";
        return "no";
    }
}