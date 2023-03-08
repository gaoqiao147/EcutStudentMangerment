package com.ecut.model;

import lombok.Data;

/**
 * @author zhouwei
 */
@Data
public class RedisLockProduct {
    private String id;
    private String productId;
    private Integer productNumber;
}
