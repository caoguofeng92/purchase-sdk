package com.camelot.purchase.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.camelot.purchase.convert.UserConvert;
import com.camelot.purchase.dao.UserMapper;
import com.camelot.purchase.domain.UserDomain;
import com.camelot.purchase.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Admin
 * @date 2022/6/8
 */
@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;


    public List<UserVO> queryList(UserDomain param) {
        List<UserDomain> domainList = userMapper.queryList(param);
        List<UserVO> userVOList = UserConvert.INSTANCT.domainListToVoList(domainList);
        log.info("查询userList={}", JSON.toJSONString(userVOList));
        return userVOList;
    }

    public UserVO findByUserName(String userName) {
        QueryWrapper<UserDomain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        UserDomain userDomain = userMapper.selectOne(queryWrapper);
        UserVO userVO = UserConvert.INSTANCT.domainToVo(userDomain);
        return userVO;
    }
}
