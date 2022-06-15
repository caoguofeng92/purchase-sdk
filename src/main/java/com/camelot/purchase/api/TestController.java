package com.camelot.purchase.api;

import com.camelot.purchase.domain.UserDomain;
import com.camelot.purchase.service.UserService;
import com.camelot.purchase.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @GetMapping("user/list")
    public List<UserVO> queryUserList(UserDomain userDomain) {
        return userService.queryList(userDomain);
    }
}
