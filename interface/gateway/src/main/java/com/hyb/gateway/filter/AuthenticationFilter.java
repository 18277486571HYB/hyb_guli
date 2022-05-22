package com.hyb.gateway.filter;

import com.hyb.gateway.bean.TokenInfo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.LinkedHashSet;
import java.util.Set;



//@Component("a")
@Order(1)  //设置执行优先级，在 全局权限认证过滤器 之前执行
public class AuthenticationFilter implements GlobalFilter, InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    private static Set<String> shouldSkipUrl = new LinkedHashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 在类被初始化完成时，把不拦截认证的请求放入集合
        shouldSkipUrl.add("/oauth/token");
        shouldSkipUrl.add("/oauth/check_token");
        shouldSkipUrl.add("/servicecms/");
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取request请求
        String requestPath = exchange.getRequest().getURI().getPath();

        //如果请求url不需要认证，直接跳过
        if(shouldSkip(requestPath)) {
            return chain.filter(exchange);
        }

        //获取Authorization请求头
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        //Authorization请求头为空，抛异常
        if(StringUtils.isEmpty(authHeader)) {
            authHeader=StringUtils.substringAfter(getToken(), "bearer ");
            //            throw new RuntimeException("请求头为空");
        }

        TokenInfo tokenInfo=null;
        try {
            //往授权服务发http请求 /oauth/check_token 并封装返回结果！
            tokenInfo = getTokenInfo(authHeader);
        }catch (Exception e) {
            throw new RuntimeException("校验令牌异常");
        }
        // 把返回的tokenInfo类，放进全局过滤器的交换器exchange中，
        // 后续可以在别的全局过滤器中取出tokenInfo信息！
        exchange.getAttributes().put("tokenInfo",tokenInfo);
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

    private TokenInfo getTokenInfo(String authHeader) {
        // 往授权服务发请求 /oauth/check_token
        // 获取token的值
        String token = StringUtils.substringAfter(authHeader, "bearer ");

        //组装请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //必须设置 basicAuth为对应的 clienId、 clientSecret
        headers.setBasicAuth("admin", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        //往授权服务发http请求 /oauth/check_token
        ResponseEntity<TokenInfo> response = restTemplate.exchange("http://localhost:8085/uac/oauth/check_token", HttpMethod.POST, entity, TokenInfo.class);
        //获取响应结果 TokenInfo
        return response.getBody();
    }

    private String getToken() {

        //组装请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //必须设置 basicAuth为对应的 clienId、 clientSecret
        headers.setBasicAuth("admin", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", null);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        //往授权服务发http请求 /oauth/check_token
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8085/uac/oauth/check_token", HttpMethod.POST, entity, String.class);
        //获取响应结果 TokenInfo
        return response.getBody();
    }
}

