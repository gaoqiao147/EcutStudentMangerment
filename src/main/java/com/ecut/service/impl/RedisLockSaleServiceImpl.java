package com.ecut.service.impl;

import com.ecut.common.Redis;
import com.ecut.mapper.RedisLockProductMapper;
import com.ecut.mapper.RedisLockSaleMapper;
import com.ecut.model.RedisLockProduct;
import com.ecut.model.RedisLockSale;
import com.ecut.service.RedisLockSaleService;
import com.ecut.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author zhouwei
 */
@Service
@Slf4j
public class RedisLockSaleServiceImpl implements RedisLockSaleService {
    @Resource
    RedisLockProductMapper redisLockProductMapper;
    @Resource
    RedisLockSaleMapper redisLockSaleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public Integer sale(RedisLockSale redisLockSale) {
        int res = 0;
        JedisPool jedisPool = RedisUtil.open(Redis.host, Redis.port);
        Jedis jedis = jedisPool.getResource();
        final String key = redisLockSale.getUserId() + "-lock";
        final String value = UUID.randomUUID().toString();
        String set = jedis.set(key, value, new SetParams().nx().ex(10));
        if ("OK".equals(set)) {
            try {
                RedisLockProduct productInfo = redisLockProductMapper.getProductInfo(redisLockSale.getProductId());
                log.info("商品信息{}", productInfo);
                if (null == productInfo) {
                    throw new Exception("无此商品！");
                } else {
                    Integer productNumber = productInfo.getProductNumber();
                    if (productNumber > 0) {
                        RedisLockSale userIdInfo = redisLockSaleMapper.getUserIdInfo(redisLockSale.getUserId());
                        if (null != userIdInfo) {
                            throw new RuntimeException("此用户已经抢购过!");
                        } else {
                            res = redisLockProductMapper.updateProduct(productNumber - 1, productInfo.getId());
                            redisLockSale.setId(UUID.randomUUID().toString());
                            res = redisLockSaleMapper.save(redisLockSale) + res;
                        }
                    } else {
                        throw new Exception("商品已被抢购完！");
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                if (value.equals(jedis.get(key))) {
                    jedis.del(key);
                }
            }
        }
        return res;
    }
}
