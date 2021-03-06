package com.camelot.purchase.api;

import com.camelot.purchase.domain.UserDomain;
import com.camelot.purchase.service.UserService;
import com.camelot.purchase.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Admin
 * @date 2022/6/6
 */
@RestController
@RequestMapping("api/test")
@Slf4j
public class TestController {
    @Value("${env.flg}")
    private String envFlg;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("aaa")
    public String test() {
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(() -> {
                log.info("test time={}", System.currentTimeMillis());
            });
        }
        return "success";

    }

    @GetMapping("env")
    public String testEnv() {
        return envFlg;

    }
    @GetMapping("exception")
    public String testException() {
        System.out.println(1/0);
        return envFlg;

    }
    @GetMapping("context")
    public String testContext() {
        RedisTemplate redisTemplate = applicationContext.getBean("redisTemplate",RedisTemplate.class);
        return ObjectUtils.identityToString(redisTemplate);

    }

    @GetMapping("redis")
    public String redis() {
        String redisKey = "testkey";
        redisTemplate.opsForValue().set(redisKey,"123456");
        return (String)redisTemplate.opsForValue().get(redisKey);

    }

    @GetMapping("user/list")
    public List<UserVO> queryUserList(UserDomain userDomain) {
        return userService.queryList(userDomain);
    }

    public static void main(String[] args) {
        System.out.println(1 << 8);
    }
}
