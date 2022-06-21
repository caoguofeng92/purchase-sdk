package com.camelot.purchase.filter;

import cn.hutool.core.util.StrUtil;
import com.camelot.purchase.common.constants.AuthConstant;
import com.camelot.purchase.common.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Admin
 * @date 2022/6/20
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    private TokenUtils tokenUtils;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //配置的自定义标识 Authorization
        String token = request.getHeader(AuthConstant.AUTH_HEADER);
        //Bearer （有个空格）标识
        if (StrUtil.isNotBlank(token) && token.startsWith(AuthConstant.AUTH_HEADER_PRE)) {
            //生成的token中带有Bearer 标识，去掉标识后就剩纯粹的token了。
            String substring = token.substring(AuthConstant.AUTH_HEADER_PRE.length());
            try {
                //解析token拿到我们生成token的时候存进去的username
                Claims claims = tokenUtils.parseToken(substring);
                String userName = (String) claims.get("username");
                if (StrUtil.isNotBlank(userName)) {
                    //将查询到的用户信息取其账号（登录凭证）以及密码去生成一个Authentication对象
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null,null);
                    //将Authentication对象放进springsecurity上下文中（进行认证操作）
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }catch (Exception e){
                //这里在解析错误token或token过期时会报异常
                log.error("解析token异常",e);
            }
        }
        //走下一条过滤器
        chain.doFilter(request,response);
    }
}
