package com.hyb.servicecms.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CrmBannerQuery {

    private String id;
    private String title;
    private String sort;
    private Date gmtCreate;
    private Date gmtModified;

}
