package com.newbee.maggie.service.impl;

import com.newbee.maggie.entity.User;
import com.newbee.maggie.mapper.UserMapper;
import com.newbee.maggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByNickname(String nickname) {
        return userMapper.findUserByUsername(nickname);
    }
}
