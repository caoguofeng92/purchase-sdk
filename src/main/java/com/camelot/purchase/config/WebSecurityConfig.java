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

    //?????????????????????????????????
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
        http.cors().and().csrf().disable() //????????????????????????????????????csrf
                .authorizeRequests()
                //???????????????
                .antMatchers(URL_WHITELISTS).permitAll()
                //??????????????????????????????
                .anyRequest().authenticated()
                //??????
                .and()
                //??????session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //??????
                .formLogin()
                //??????????????????
                .successHandler(successHandler())
                //??????????????????
                .failureHandler(failureHandler());

    }


    /**
     * ?????????????????????
     *
     * @return
     */
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException, IOException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                //??????????????????????????????
                String userName = authentication.getName();
                UserVO userInfo = userService.findByUserName(userName);
                Map<String, Object> claims = new HashMap<>();
                //????????????????????????token?????????
                claims.put("username", userInfo.getUserName());
                //??????token
                String token = tokenUtils.createToken(claims);
                httpServletResponse.addHeader("jwt_token", token);
                PrintWriter writer = httpServletResponse.getWriter();
                //???token???????????????????????????????????????
                writer.println(ResultUtil.reSuccess(userInfo));
                //????????????????????????
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     * ?????????????????????
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
                writer.println(ResultUtil.reFailure("????????????"));
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     * ???????????????
     *
     * @return
     */
    public AuthenticationEntryPoint AuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {


            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                //????????????????????? /login ?????????????????????json?????????
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println(ResultUtil.reFailure("?????????????????????????????????????????????"));
                writer.flush();
                writer.close();
            }
        };
    }
}
