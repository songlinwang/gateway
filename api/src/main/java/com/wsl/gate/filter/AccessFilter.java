package com.wsl.gate.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wsl
 * @date 2019/1/7
 */
@Component
public class AccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        /**
         * pre 为路由前
         * route 为路由过程中
         * post 为路由过程后
         * error 为出现错误的时候
         */
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        /**
         * 是否执行这个过滤器，返回true 则表示执行这个过滤器
         */
        return true;
    }

    @Override
    public Object run() {
        // 获取当前请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String access_token = request.getHeader("accessToken");
        if (StringUtils.isEmpty(access_token)) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return access_token;
    }
}
