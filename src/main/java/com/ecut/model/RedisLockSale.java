package com.ecut.model;

import lombok.Data;

/**
 * @author zhouwei
 */
@Data
public class RedisLockSale {
    private String id;
    private String productId;
    private String userId;
}
