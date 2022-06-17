package com.camelot.purchase.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Admin
 * @date 2022/6/8
 */
@Data
@TableName("t_user")
public class UserDomain {

    private Long id;
    private String userNumber;
    private String userName;
    private String password;
    private Integer delFlg;
}
