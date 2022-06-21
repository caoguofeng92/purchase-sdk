package com.camelot.purchase.api;

import com.camelot.purchase.vo.UserVO;
import org.openjdk.jol.info.ClassLayout;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Admin
 * @date 2022/6/16
 */
@RestController
public class HelloController {

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }
    @GetMapping("/user/index")
    public String index() {
        return "hello index";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123"));
        UserVO userVO = new UserVO();
        userVO.setId(8L);
        System.out.println(ClassLayout.parseInstance(userVO).toPrintable());
    }
}

