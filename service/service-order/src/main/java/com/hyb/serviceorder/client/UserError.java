package com.hyb.serviceorder.client;

import com.hyb.CommonUtil.UcenterMember;
import org.springframework.stereotype.Component;

@Component
public class UserError implements UserClient{
    @Override
    public UcenterMember getUserInfo(String header) {
        return null;
    }
}
