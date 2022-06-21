package com.camelot.purchase.config;

import com.camelot.purchase.common.utils.ResultUtil;
import com.camelot.purchase.common.utils.TokenUtils;
import com.camelot.purchase.service.UserService;
import com.camelot.purchase.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Admin
 * @date 2022/6/15WebSecurityConfig
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService oauthUserService;

    @Autowired
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    //这里是定义了放行的路径
    private static final String[] URL_WHITELISTS = {
            "/login/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/api/**"
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(oauthUserService).passwordEncoder(passwordEncoder);
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http.antMatcher("/api/**").authorizeRequests()
////                .antMatchers("/oauth/**","user/**").permitAll()
////                .and().csrf().disable();
//        http.exceptionHandling().accessDeniedPage("/unauth.html");
//        http.formLogin()
//                .loginPage("/login.html").loginProcessingUrl("/user/login")
//                .defaultSuccessUrl("/user/index").permitAll()
//                .and().authorizeRequests().antMatchers("user/**").permitAll()
//                .antMatchers("/user/hello").hasAuthority("admin")
//                .anyRequest().authenticated()
//                .and().csrf().disable();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable() //这里是解决跨域问题和禁用csrf
                .authorizeRequests()
                //白名单放行
                .antMatchers(URL_WHITELISTS).permitAll()
                //其他所有请求都要认证
                .anyRequest().authenticated()
                //连接
                .and()
                //关闭session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //登录
                .formLogin()
                //登录成功处理
                .successHandler(successHandler())
                //登录失败处理
                .failureHandler(failureHandler());

    }


    /**
     * 登录成功处理器
     *
     * @return
     */
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException, IOException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                //取出此时登录的用户名
                String userName = authentication.getName();
                UserVO userInfo = userService.findByUserName(userName);
                Map<String, Object> claims = new HashMap<>();
                //我们将用户名存到token当中去
                claims.put("username", userInfo.getUserName());
                //生成token
                String token = tokenUtils.createToken(claims);
                httpServletResponse.addHeader("jwt_token", token);
                PrintWriter writer = httpServletResponse.getWriter();
                //将token包装到同一的返回结果类返回
                writer.println(ResultUtil.reSuccess(userInfo));
                //刷新确保成功响应
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     * 登录失败处理器
     *
     * @return
     */
    public AuthenticationFailureHandler failureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println(ResultUtil.reFailure("登录失败"));
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     * 认证处理器
     *
     * @return
     */
    public AuthenticationEntryPoint AuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {


            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                //前后端分离项目 /login 可以是返回一个json字符串
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println(ResultUtil.reFailure("未登录！或登录失效请重新登录！"));
                writer.flush();
                writer.close();
            }
        };
    }
}
