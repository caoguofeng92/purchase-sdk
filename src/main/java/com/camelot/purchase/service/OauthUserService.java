package com.camelot.purchase.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.camelot.purchase.dao.UserMapper;
import com.camelot.purchase.domain.UserDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Admin
 * @date 2022/6/17
 */
@Slf4j
@Component
public class OauthUserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO find in db
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("1234567");
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        QueryWrapper<UserDomain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",username);
        UserDomain userDomain = userMapper.selectOne(queryWrapper);
        if(userDomain == null){
            throw new UsernameNotFoundException(String.format("用户%s查询不存在",username));
        }
        User user = new User(userDomain.getUserName(),userDomain.getPassword(),authorityList);
        return user;
    }
}
