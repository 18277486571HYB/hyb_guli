package com.hyb.servicecenter.entity.vo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpecCaptchaVo implements InitializingBean {

    @Value("${default.captcha.width}")
    private int wd;

    @Value("${default.captcha.height}")
    private int he;

    @Value("${default.captcha.length}")
    private int len;

    public static int width;
    public static int height;
    public static int length;

    @Override
    public void afterPropertiesSet() {
        width=this.wd;
        height=this.he;
        length=this.len;
    }
}
