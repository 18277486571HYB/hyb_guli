package com.hyb.serviceware.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-order")
public interface OrderClient {

    @GetMapping("/getStatus/{orderNo}")
    public Integer getStatus(@PathVariable("orderNo") String orderNo);
}
