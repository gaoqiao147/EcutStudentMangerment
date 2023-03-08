package com.ecut.mapper;

import com.ecut.model.LoginDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouwei
 * @since 2022-06-25
 */
public interface LoginMapper extends BaseMapper<LoginDO> {
    /**
     * 注册
     *
     * @param login
     * @return
     */
    int registered(@Param("login") LoginDO login);

    /**
     * 查询用户
     *
     * @param usernumber
     * @return
     */
    LoginDO info(@Param("usernumber") Integer usernumber);

    /**
     * 用户名登录
     * @param username
     * @return
     */
    LoginDO  loadByUserName(@Param("username") String username);

    /**
     * 查询所有用户
     * @return
     */
    List<LoginDO> selectAll();
    /**
     * 批量插入
     * @param list
     * @return
     */
    int saveLogins(@Param("list") List<LoginDO> list);

}
