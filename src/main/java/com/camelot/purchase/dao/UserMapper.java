package com.camelot.purchase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.camelot.purchase.domain.UserDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Admin
 * @date 2022/6/7
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDomain> {

    List<UserDomain> queryList(UserDomain userDomain);
}
