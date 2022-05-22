package com.hyb.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class TokenUtils {

    @Autowired
    RestTemplate restTemplate;

    public String getToken(Map<String,String> map) throws JSONException {

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.put("grant_type", Collections.singletonList(map.get("grant_type")));
        paramsMap.put("scope", Collections.singletonList(map.get("scope")));
        paramsMap.put("client_id", Collections.singletonList(map.get("client_id")));
        paramsMap.put("client_secret", Collections.singletonList(map.get("client_secret")));
        paramsMap.put("username", Collections.singletonList(map.get("username")));
        paramsMap.put("password", Collections.singletonList(map.get("password")));

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<MultiValueMap<String,String>>(paramsMap,headers);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                //只要重写此方法，不去抛出HttpClientErrorException异常即可
                HttpStatus statusCode = clientHttpResponse.getStatusCode();
                System.out.println("错误码 = "+statusCode);
            }
        });
//        JSONObject response = template.postForObject(url, paramsMap, JSONObject.class);
        JSONObject response = restTemplate.exchange(map.get("url"), HttpMethod.POST, httpEntity, JSONObject.class,new Object[]{}).getBody();

        return (String) response.get("access_token");
    }

}
