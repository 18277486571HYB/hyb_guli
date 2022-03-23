package com.hyb.serviceedu.client;

import com.hyb.CommonUtil.Msg;

import com.hyb.CommonUtil.UcenterMember;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import javax.servlet.http.HttpServletRequest;

@FeignClient("service-center")
public interface CommentClient {

    @GetMapping(value = "/servicecenter/ucenterMember/getUser",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Msg getUser(HttpServletRequest httpServletRequest);

    @GetMapping(value = "/servicecenter/ucenterMember/getUserInfo/{header}")
    public UcenterMember getUserInfo(@PathVariable("header")String header);


}
