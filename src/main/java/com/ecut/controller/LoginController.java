package com.ecut.controller;


import cn.hutool.http.HttpResponse;
import com.ecut.annotation.MyAnnotation;
import com.ecut.model.LoginDO;
import com.ecut.service.LoginService;
import com.ecut.util.CommonResult;
import com.ecut.vo.ResultVo;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhouwei
 * @since 2022-06-25
 */
@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @MyAnnotation
    @PostMapping("/verify")
    public Object loginVerify(@RequestBody LoginDO loginDO, HttpServletRequest request) throws Exception {

        return loginService.loginVerification(loginDO);
    }

    @PostMapping
    public ResultVo register(@RequestBody LoginDO loginDO){
        Integer registered = loginService.registered(loginDO);
        if(registered == 0){
            return CommonResult.fail();
        }else {
            return CommonResult.success(loginDO);
        }
    }
}

