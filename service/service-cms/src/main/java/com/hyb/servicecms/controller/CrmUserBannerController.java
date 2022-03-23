package com.hyb.servicecms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.CommonUtil.Msg;
import com.hyb.servicecms.entity.CrmBanner;
import com.hyb.servicecms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-03-01
 */
@Api(tags = "banner用户controller")
@RestController
@RequestMapping("/servicecms/crmBanner/user")
public class CrmUserBannerController {

    @Autowired
    CrmBannerService crmBannerService;

    //列出全部banner图
    @ApiOperation("轮播banner图")
    @GetMapping("/listAllBanner/{limit}")
    public Msg listAllBanner(@PathVariable(required = false) Integer limit){
        if (StringUtils.isEmpty(limit)){
            limit=5;
        }
        QueryWrapper<CrmBanner> crmBannerQueryWrapper = new QueryWrapper<>();
        // 根据排序字段进行升序排序,小的排在最前面
//        crmBannerQueryWrapper.orderByAsc("sort");
        // 拼接sql语句,将最前面的五条数据展示
        crmBannerQueryWrapper.last("limit "+limit);
        List<CrmBanner> list = crmBannerService.getList(crmBannerQueryWrapper);
        return list.size()==0?Msg.fail().message("数据为空"):Msg.success().data("list",list);
    }
}

