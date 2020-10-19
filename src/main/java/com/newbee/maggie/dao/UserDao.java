package com.newbee.maggie.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    public int getUserByLoginName(@Param("nickname") String nickname, @Param("contact_information") String contact_information) {
        return 1;
    }
}
