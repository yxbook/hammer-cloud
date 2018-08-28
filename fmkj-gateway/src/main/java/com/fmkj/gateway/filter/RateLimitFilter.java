package com.fmkj.gateway.filter;

import com.fmkj.gateway.exception.RateLimitException;
import com.fmkj.gateway.util.CookieUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;
/**
 * @Auther: youxun
 * @Date: 2018/8/24 21:03
 * @Description:  1、限流拦截器、使用令牌桶算法，限流优先级最高
 *                2、权限拦截
 *                3、其他拦截
 *
 */

@Component
public class RateLimitFilter extends ZuulFilter{

    // 每秒钟放置100个令牌
    private  static final RateLimiter RATE_LIMITER = RateLimiter.create(100);


    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 1、限流拦截器、使用令牌桶算法，限流优先级最高
        System.out.println("============================》》》拿到令牌啦12312");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        // 没有拿到令牌处理方法
        if(!RATE_LIMITER.tryAcquire()){
            System.out.println("============================》》》没拿到令牌啦");
            throw new RateLimitException();
        }

        //2、权限拦截
        Cookie cookie = CookieUtils.getCookie(request, "Token");

        //3、其他拦截  例如：控制不同的人访问不同的方法

        return null;
    }
}
