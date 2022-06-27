package com.hyb.serviceorder.client;

import com.hyb.CommonUtil.Msg;
import com.hyb.serviceorder.entity.TOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "service-ware" ,fallback = WareError.class)
public interface WareClient  {

    @PostMapping("/lock")
    public Msg lock(@RequestBody TOrder tOrder);

    @PostMapping("/delWare")
    public Msg delWare(@RequestBody TOrder tOrder);

}
