package com.ecut.service;

import com.ecut.model.LoginDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecut.model.SecurityUser;
import com.ecut.vo.ResultVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhouwei
 * @since 2022-06-25
 */
public interface LoginService extends IService<LoginDO> {
    /**
     * 登陆验证
     *
     * @param loginDO 登录实体类
     * @return
     * @throws Exception
     */
    Object loginVerification(LoginDO loginDO) throws Exception;

    /**
     * 注册（redis锁）
     *
     * @param loginDO
     * @return
     */
    Integer registered(LoginDO loginDO);

    /**
     * 查询用户
     * @param username
     * @return
     */
    SecurityUser loadByUserName(String username);
}
