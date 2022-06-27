package com.hyb.gateway.filter;

import com.alibaba.fastjson.JSONException;


import com.google.gson.JsonObject;
import com.hyb.CommonUtil.JwtUtils;
import com.hyb.CommonUtil.TokenUtils;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.gateway.bean.TokenInfo;
import com.hyb.gateway.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component("a")
@Order(2)
 //设置执行优先级，在 全局权限认证过滤器 之前执行
public class AuthenticationFilter implements GlobalFilter, InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    RedisUtils redisUtils;

    private static Set<String> shouldSkipUrl = new LinkedHashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 在类被初始化完成时，把不拦截认证的请求放入集合
        shouldSkipUrl.add("/uac");
        shouldSkipUrl.add("/serviceedu/front/listTeacher");

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
        String token = exchange.getRequest().getHeaders().getFirst("token");
        //Authorization请求头为空，抛异常
        if(StringUtils.isEmpty(token)) {
            return out(exchange.getResponse());
        }

        redisUtils.set("fromUrl",requestPath,10, TimeUnit.MINUTES);

        if (!JwtUtils.checkToken(token)){
            exchange.getResponse().setStatusCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            return exchange.getResponse().setComplete();
        }

        Claims memberClaims = JwtUtils.getMemberClaims(token);
        if (memberClaims==null)
            return null;
        String nickname = (String) memberClaims.get("nickname");

        Set<String> set = redisUtils.getSet(nickname);

        if (!hasPermisson(set,requestPath)){
            exchange.getResponse().setStatusCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            return exchange.getResponse().setComplete();
        }


//        TokenInfo tokenInfo=null;
//        try {
//            //往授权服务发http请求 /oauth/check_token 并封装返回结果！
//            tokenInfo = getTokenInfo(authHeader);
//        }catch (Exception e) {
//            throw new RuntimeException("校验令牌异常");
//        }
//        // 把返回的tokenInfo类，放进全局过滤器的交换器exchange中，
//        // 后续可以在别的全局过滤器中取出tokenInfo信息！
//        exchange.getAttributes().put("tokenInfo",tokenInfo);
        return chain.filter(exchange);
    }

    private Mono<Void> out(ServerHttpResponse response) {
        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", 20001);
        message.addProperty("data", "鉴权失败");
        byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


    private boolean hasPermisson(Set<String> set,String path){
        for (String s :
                set) {
            if (path.contains(s))
                return true;
        }
        return false;
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

//    private String getToken() throws Exception {
//        Map<String,String> map=new HashMap<>();
//        map.put("grant_type","password");
//        map.put("client_id","admin");
//        map.put("client_secret","123456");
//        map.put("username","admin");
//        map.put("password","123456");
//        String token = tokenUtils.getToken(map);
//
//    }
}

