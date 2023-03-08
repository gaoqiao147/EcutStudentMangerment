package com.ecut.service;

import com.ecut.model.RedisLockSale;

/**
 * @author zhouwei
 */
public interface RedisLockSaleService {
    /**
     * 抢购
     *
     * @param redisLockSale
     * @return
     * @throws Exception
     */
    Integer sale(RedisLockSale redisLockSale) throws Exception;
}
