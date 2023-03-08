package com.ecut.controller;

import com.ecut.model.LoginDO;
import com.ecut.model.RedisLockSale;
import com.ecut.service.RedisLockSaleService;
import com.ecut.util.CommonResult;
import com.ecut.vo.ResultVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhouwei
 */
@RestController
@RequestMapping("redis-lock-sale")
public class RedisLockSaleController {
    @Resource
    RedisLockSaleService redisLockSaleService;

    @PostMapping
    public ResultVo sale(@RequestBody RedisLockSale redisLockSale) throws Exception {
        Integer res = redisLockSaleService.sale(redisLockSale);
        if(res == 0){
            return CommonResult.fail();
        }else {
            return CommonResult.success(redisLockSale);
        }
    }
}
