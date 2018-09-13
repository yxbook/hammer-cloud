package com.fmkj.gateway.filter;

import com.fmkj.common.base.BaseResult;
import com.fmkj.common.base.BaseResultEnum;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;
import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * @Auther: youxun
 * @Date: 2018/8/24 21:03
 * @Description:  1、限流拦截器、使用令牌桶算法，限流优先级最高
 *                2、权限拦截
 *                3、其他拦截
 *
 */

@Component
public class AccessTokenFilter extends ZuulFilter{

    // 每秒钟放置100个令牌
    private  static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Autowired
    private UserApi userApi;

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
    public Object run(){

        try {
            // 1、限流拦截器、使用令牌桶算法，限流优先级最高
            RequestContext requestContext = RequestContext.getCurrentContext();
            HttpServletRequest request = requestContext.getRequest();
            // 没有拿到令牌处理方法
            if(!RATE_LIMITER.tryAcquire()){
                return new BaseResult<Boolean>(BaseResultEnum.NOACCESS, false);
            }

            RequestContext context = getCurrentContext();

            InputStream in  = request.getInputStream();

            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));

            System.out.println("BODY:" + body);
            final byte[] reqBodyBytes = body.getBytes();
            context.setRequest(new HttpServletRequestWrapper(getCurrentContext().getRequest()) {
                @Override
                public ServletInputStream getInputStream() throws IOException {
                    return new ServletInputStreamWrapper(reqBodyBytes);
                }

                @Override
                public int getContentLength() {
                    return reqBodyBytes.length;
                }

                @Override
                public long getContentLengthLong() {
                    return reqBodyBytes.length;
                }
            });
           /* context.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
            BaseResult<Boolean> result = new BaseResult<Boolean>(BaseResultEnum.NOACCESS, false);
            context.setResponseBody(JSON.toJSONString(result));// 返回错误内容
            HttpServletResponse response = context.getResponse();
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setLocale(new java.util.Locale("zh","CN"));*/

        } catch (IOException e) {
            rethrowRuntimeException(e);
        }
        return null;
    }
}