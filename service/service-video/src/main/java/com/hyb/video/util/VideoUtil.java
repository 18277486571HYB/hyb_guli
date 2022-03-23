package com.hyb.video.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VideoUtil implements InitializingBean {

    @Value("${aliyun.video.file.keyid}")
    private String key;

    @Value("${aliyun.video.file.keysecret}")
    private String value;

    @Value("${aliyun.video.file.cateId}")
    private String cateid;

    @Value("${aliyun.video.file.templateGroupId}")
    private String templategroupid;

    public static String accessKey;
    public static String accesskeyValue;
    public static String cateId;
    public static String templateGroupId;

    @Override
    public void afterPropertiesSet() throws Exception {
        accessKey=key;
        accesskeyValue=value;
        cateId=cateid;
        templateGroupId=templategroupid;
    }
}
