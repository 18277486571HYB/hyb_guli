package com.hyb.serviceorder.client;

import com.hyb.CommonUtil.Msg;
import com.hyb.serviceorder.entity.TOrder;
import org.springframework.stereotype.Component;

@Component
public class WareError implements WareClient{
    @Override
    public Msg lock(TOrder tOrder) {
        return null;
    }

    @Override
    public Msg delWare(TOrder tOrder) {
        return null;
    }
}