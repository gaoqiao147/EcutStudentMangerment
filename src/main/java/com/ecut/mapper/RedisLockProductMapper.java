package com.ecut.mapper;

import com.ecut.model.RedisLockProduct;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhouwei
 */
public interface RedisLockProductMapper {
    /**
     * 获取商品信息
     *
     * @param productId
     * @return
     */
    RedisLockProduct getProductInfo(@Param("productId") String productId);
    /**
     * 更新库存
     * @param productNumber
     * @param id
     * @return
     */
    int updateProduct(@Param("productNumber") Integer productNumber,@Param("id") String id);
}
