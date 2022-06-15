package com.camelot.purchase.domain;

import lombok.Data;

/**
 * @author Admin
 * @date 2022/6/8
 */
@Data
public class UserDomain {

    private Long id;
    private String userNumber;
    private String userName;
    private Integer delFlg;
}
