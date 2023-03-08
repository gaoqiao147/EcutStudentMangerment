package com.ecut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecut.common.Redis;
import com.ecut.mapper.LoginMapper;
import com.ecut.model.LoginDO;
import com.ecut.model.SecurityUser;
//import com.ecut.rabbitmq.publisher.LogPublisher;
import com.ecut.rabbitmq.publisher.LogPublisher;
import com.ecut.service.LoginService;
import com.ecut.util.JwtUtil;
import com.ecut.util.RedisUtil;
import com.ecut.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouwei
 * @since 2022-06-25
 */
@Service
@Slf4j
public class LoginServiceImpl extends ServiceImpl<LoginMapper, LoginDO> implements LoginService {
    @Resource
    private LoginMapper loginMapper;
    @Resource
    LogPublisher logPublisher;
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    AuthenticationManager authenticationManager;

    @Override
    public Object loginVerification(LoginDO loginDO) {
        //定义一个公共返回类
        ResultVo resultVo = new ResultVo<LoginDO>();
        //将前端传的值先从redis中查找，没有值就在redis中创建
        String host = "1.117.87.146";
        int port = 6379;
        try {
            JedisPool pool = RedisUtil.open(host, port);
            Jedis jedis = pool.getResource();
            if (jedis.get(loginDO.getUsernumber().toString()) != null) {
                if (jedis.get(loginDO.getUsernumber().toString()).equals(loginDO.getPassword())) {
                    resultVo.setCode(1);
                    resultVo.setData(jedis.get(loginDO.getUsernumber().toString()));
                    //发送队列消息
                    logPublisher.sendLogMsg(loginDO);
                    System.out.println("使用了redis登录成功");
                } else {
                    resultVo.setCode(-2);
                    System.out.println("使用了redis登录失败");
                }
            } else {
                //前端传递的值在是实现类验证
                QueryWrapper<LoginDO> loginQueryWrapper = new QueryWrapper<>();
                loginQueryWrapper.eq("usernumber", loginDO.getUsernumber());
                LoginDO loginVerify = loginMapper.selectOne(loginQueryWrapper);
                //loginVerify为空表示数据空中无数据, -1代表无数据
                if (loginVerify == null) {
                    resultVo.setCode(-1);
                } else {
                    //判断传入的密码是否匹配 , -2代表密码错误
                    if (!passwordEncoder.matches(loginDO.getPassword(), loginVerify.getPassword())) {
                        resultVo.setCode(-2);
                    } else {
                        resultVo.setCode(1);
                        //SecurityUser
                        //校验
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDO.getUsername(), loginDO.getPassword());
//
                        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
                        if(Objects.isNull(authenticate)){
                            throw new RuntimeException("用户名或密码错误");
                        }
                        //转换成SecurityUser
                        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
                        //生成jwt
                        String token = JwtUtil.createToken(securityUser.getUsername());
                        HashMap<String, Object> map = new HashMap();
                        map.put("token", token);

                        resultVo.setData(map);
                        //发送队列消息
                        logPublisher.sendLogMsg(loginDO);

                        return map;
                    }
                }
                //在redis创建学生信息
                jedis.setex(loginDO.getUsernumber().toString(),10*1000,loginDO.getPassword());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVo;
    }

    @Override
    public Integer registered(LoginDO loginDO) {
        int res = 0;
        JedisPool jedisPool = RedisUtil.open(Redis.host, Redis.port);
        Jedis jedis = jedisPool.getResource();
        final String key = loginDO.getUsernumber() + "-lock";
        final String value = UUID.randomUUID().toString();
        String set = jedis.set(key, value, new SetParams().nx().ex(20));
        try {
            if ("OK".equals(set)) {
                LoginDO info = loginMapper.info(loginDO.getUsernumber());
                if (info == null) {
                    log.info("注册成功{}", loginDO);
                    res = loginMapper.registered(loginDO);
                } else {
                    log.info("注册失败，用户已存在！");
                }
            } else {
                log.info("该进程被锁，无法注册");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (value.equals(jedis.get(key))) {
                jedis.del(key);
                log.info("删除锁成功！");
            }
        }
        return res;
    }

    @Override
    public SecurityUser loadByUserName(String username) {
        LoginDO loginUser = loginMapper.loadByUserName(username);
        if (loginUser != null) {
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUsername(loginUser.getUsername());
            securityUser.setPassword(loginUser.getPassword());
            String role = loginUser.getRole();
            List<String> auth = Arrays.asList(role.split(","));
            securityUser.setAuthorities(auth);
            return securityUser;
        }
        return null;
    }
}
