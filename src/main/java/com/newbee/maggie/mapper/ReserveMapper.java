package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Reserve;

import java.util.List;

public interface ReserveMapper {
    /**
     * 根据用户id查找用户预定
     * @param userId
     * @return
     */
    List<Reserve> findReserveByUserId(Integer userId);
}
