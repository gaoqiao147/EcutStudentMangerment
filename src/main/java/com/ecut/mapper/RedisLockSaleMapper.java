package com.ecut.mapper;

import com.ecut.model.RedisLockSale;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhouwei
 */
public interface RedisLockSaleMapper {
    /**
     * 获取抢购信息
     *
     * @param userId
     * @return
     */
    RedisLockSale getUserIdInfo(@Param("userId") String userId);

    /**
     * 保存抢购信息
     *
     * @param redisLockSale
     * @return
     */
    int save(@Param("redisLockSale") RedisLockSale redisLockSale);
}
