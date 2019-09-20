package com.wsl.gate.filter;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SystemPublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author wsl
 * @date 2019/7/12
 */
public class RateLimiterFilter extends ZuulFilter {

    private Map<String, RateLimiter> map = Maps.newConcurrentMap();

    @Autowired
    private SystemPublicMetrics systemPublicMetrics;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        // 这边的order一定要大于org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter的order
        // 也就是要大于5
        // 否则，RequestContext.getCurrentContext()里拿不到serviceId等数据。
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        /**
         * Spring Boot Actuator提供的Metrics 能力进行实现基于内存压力的限流  这个是网关的限流  门户的限流 微服务本身也有限流
         */
        /**
         * 是否执行这个过滤器，返回true 则表示执行这个过滤器
         */
        Collection<Metric<?>> metrics = systemPublicMetrics.metrics();
        Optional<Metric<?>> freeMemoryMetric = metrics.stream()
                .filter(t -> "mem.free".equals(t.getName()))
                .findFirst();
        // 如果不存在这个指标，稳妥起见，返回true，开启限流
        if (!freeMemoryMetric.isPresent()) {
            return true;
        }
        long freeMemory = freeMemoryMetric.get()
                .getValue()
                .longValue();
        // 如果可用内存小于1000000KB，开启流控
        return freeMemory < 1000000L;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletResponse response = context.getResponse();

            String key = null;
            // 对于service格式的路由，走RibbonRoutingFilter
            String serviceId = (String) context.get("serviceId");
            if (serviceId != null) {
                key = serviceId;
                map.putIfAbsent(serviceId, RateLimiter.create(1000.0));
            }
            // 如果压根不走RibbonRoutingFilter，则认为是URL格式的路由
            else {
                // 对于URL格式的路由，走SimpleHostRoutingFilter
                URL routeHost = context.getRouteHost();
                if (routeHost != null) {
                    String url = routeHost.toString();
                    key = url;
                    map.putIfAbsent(url, RateLimiter.create(2000.0));
                }
            }
            RateLimiter rateLimiter = map.get(key);
            if (!rateLimiter.tryAcquire()) {
                HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;

                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.setStatus(httpStatus.value());
                response.getWriter().append(httpStatus.getReasonPhrase());

                context.setSendZuulResponse(false);

                throw new ZuulException(
                        httpStatus.getReasonPhrase(),
                        httpStatus.value(),
                        httpStatus.getReasonPhrase()
                );
            }
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }
    }
}
