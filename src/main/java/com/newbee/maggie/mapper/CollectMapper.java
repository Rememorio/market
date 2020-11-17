package com.newbee.maggie.mapper;

import com.newbee.maggie.entity.Collect;

import java.util.List;

public interface CollectMapper {

    /**
     * 根据用户id查找用户收藏
     * @param userId
     * @return
     */
    List<Collect> getCollectByUserId(Integer userId);

    /**
     * 插入收藏
     * @param collect
     * @return
     */
    int insertCollect(Collect collect);

    /**
     * 删除收藏
     * @param collect
     * @return
     */
    int deleteCollect(Collect collect);
}
