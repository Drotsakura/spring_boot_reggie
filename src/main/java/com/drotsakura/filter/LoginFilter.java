package com.drotsakura.filter;

import com.alibaba.fastjson.JSON;
import com.drotsakura.common.BaseContext;
import com.drotsakura.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginFilter",urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter {
    //不需要处理的请求路径
    private static final String[] paths = {"/backend/**","/front/**","/employee/login","/employee/logout","/user/sendMsg","/user/login"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //获得当前用户请求路径
        String requestURI = httpServletRequest.getRequestURI();
        //判断当前请求不需要处理
        boolean check = check(requestURI);
        //不需要处理
        if (check){
            //放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //需要处理的请求,判断是否登录
        if (httpServletRequest.getSession().getAttribute("employee") != null){
            Long id = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.setId(id);
            //已经登录，放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        //需要处理的请求,判断是否登录
        if (httpServletRequest.getSession().getAttribute("user") != null){
            Long id = (Long) httpServletRequest.getSession().getAttribute("user");
            log.debug("userid-filter:{}",id);
            BaseContext.setId(id);
            //已经登录，放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        //没有登录，返回错误信息
        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    private boolean check(String requestURI){
        //路径解析器
        AntPathMatcher matcher = new AntPathMatcher();
        //路径请求处理
        for (String path : paths) {
            boolean match = matcher.match(path, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
