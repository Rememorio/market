package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Reserve;

import java.util.List;

public interface ReserveMapper {
    /**
     * 根据用户id查找用户预定
     * @param userId
     * @return
     */
    List<Reserve> getReserveByUserId(Integer userId);

    /**
     * 根据商品id查找预定信息
     * @param cmId
     * @return
     */
    Reserve getReserveByCmId(Integer cmId);

    /**
     * 获取Reserve表元组数，方便生成reserveId
     * @return
     */
    Integer getIdCount();

    /**
     * 获取最大id
     * @return
     */
    Integer getMaxId();

    /**
     * 获取cmId对应的reserveId，方便生成订单编号
     * @param cmId
     * @return
     */
    Integer getReserveIdByCmId(Integer cmId);

    /**
     * 获取cmId对应的reservTime，方便生成
     * @param cmId
     * @return
     */
    String getReserveTimeByCmId(Integer cmId);

    /**
     * 插入预订
     * @param reserve
     * @return
     */
    int insertReserve(Reserve reserve);

    /**
     * 删除预订
     * @param cmId
     * @return
     */
    int deleteReserve(Integer cmId);

    /**
     * 把userId置null，从而实现删除预订
     * @param cmId
     * @return
     */
    int updateReserve(Integer cmId);
}
