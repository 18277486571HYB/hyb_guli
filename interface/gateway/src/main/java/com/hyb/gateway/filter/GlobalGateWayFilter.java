package com.hyb.gateway.filter;

import com.hyb.gateway.bean.TokenInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

//权限认证过滤器

//@Component("b")
@Order(2)
public class GlobalGateWayFilter implements GlobalFilter, InitializingBean {


    private static Set<String> shouldSkipUrl = new LinkedHashSet<>();
    @Override
    public void afterPropertiesSet() throws Exception {
        // 不拦截认证的请求
        shouldSkipUrl.add("/oauth/token");
        shouldSkipUrl.add("/oauth/check_token");
        shouldSkipUrl.add("/user/getCurrentUser");

    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request请求
        String requestPath = exchange.getRequest().getURI().getPath();
        //如果请求url不需要认证，直接跳过
        if(shouldSkip(requestPath)) {
            return chain.filter(exchange);
        }
        //从全局过滤器的交换器exchange中取出 之前放入的tokenInfo信息！
        TokenInfo tokenInfo = exchange.getAttribute("tokenInfo");

        if(!tokenInfo.isActive()) {
            throw new RuntimeException("token过期");
        }

        //根据tokenInfo信息，鉴权！
        hasPremisson(tokenInfo,requestPath);

        return chain.filter(exchange);
    }

    private boolean shouldSkip(String reqPath) {

        for(String skipPath:shouldSkipUrl) {
            if(reqPath.contains(skipPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPremisson(TokenInfo tokenInfo,String currentUrl) {
        boolean hasPremisson = false;
        //登录用户所拥有的请求url权限集合
        List<String> premessionList = Arrays.asList(tokenInfo.getAuthorities());
        //与当前请求url，看是否有对应的访问权限
        for (String url: premessionList) {
            if(currentUrl.contains(url)) {
                hasPremisson = true;
                break;
            }
        }
        //如果没有，抛异常
        if(!hasPremisson){
            throw new RuntimeException("没有权限");
        }
        return hasPremisson;
    }
}

