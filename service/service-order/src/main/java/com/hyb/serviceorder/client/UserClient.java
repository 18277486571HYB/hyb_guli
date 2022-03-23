package com.hyb.serviceorder.client;

import com.hyb.CommonUtil.Msg;
import com.hyb.CommonUtil.UcenterMember;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(value = "service-center")
public interface UserClient {
    @GetMapping(value = "/servicecenter/ucenterMember/getUserInfo/{header}")
    public UcenterMember getUserInfo(@PathVariable("header")String header);

}
