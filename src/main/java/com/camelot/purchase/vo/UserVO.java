package com.camelot.purchase.vo;

import lombok.Data;

/**
 * @author Admin
 * @date 2022/6/8
 */
@Data
public class UserVO {

    private Long id;
    private String userNumber;
    private String userName;
    private Integer delFlg;
    private String password;
}
