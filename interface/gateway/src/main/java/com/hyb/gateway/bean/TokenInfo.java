package com.hyb.gateway.bean;

import lombok.Data;

import java.util.Date;

@Data
public class TokenInfo {

    private boolean active;

    private String client_id;

    private String[] scope;

    private String username;

    private String[] aud;
    //过期时间
    private Date exp;
    //该token的授权信息
    private String[] authorities;

}

