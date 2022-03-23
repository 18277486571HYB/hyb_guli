package com.hyb.serviceedu.client;

import com.hyb.CommonUtil.Msg;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-video")
public interface VideoClient {

    @DeleteMapping("/serviceVideo/serviceVideo/delVideo/{id}")
    public Msg delVideo(@PathVariable("id") String id);

    @DeleteMapping("/serviceVideo/serviceVideo/delBatchVideo")
    public Msg delBatchVideo(@RequestParam("list") List<String> list);

}
