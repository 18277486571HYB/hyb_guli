package com.hyb.servicecms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyb.CommonUtil.ErrorUtil;
import com.hyb.CommonUtil.Msg;
import com.hyb.servicecms.entity.CrmBanner;
import com.hyb.servicecms.entity.vo.CrmBannerQuery;
import com.hyb.servicecms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-03-01
 */
@Api(tags = "banner管理员controller")
@RestController
@RequestMapping("/servicecms/crmBanner/management")
@CrossOrigin
public class CrmManagementBannerController {

    @Autowired
    CrmBannerService crmBannerService;

    //id查询
    @ApiOperation("banner图id查询")
    @GetMapping("/getBanner/{id}")
    public Msg getBanner(@ApiParam(value = "banner的id",name = "id")@PathVariable String id){
        CrmBanner banner = crmBannerService.getById(id);
        return banner==null?Msg.fail().message("banner图为空"):Msg.success().data("banner",banner);
    }

    //条件查询
    @ApiOperation("banner图条件分页查询")
    @PostMapping("/listBannerForKey/{current}/{limit}")
    public Msg listBannerForKey(
            @ApiParam(value = "当前页",name = "current") @PathVariable(required = false) Integer current,
            @ApiParam(value = "每页最大数量",name="limit")@PathVariable(required = false) Integer limit,
            @ApiParam(value = "查询条件",name = "crmBannerQuery")@RequestBody(required = false) CrmBannerQuery crmBannerQuery){
        if (StringUtils.isEmpty(current)) {
            current=1;
        }
        if (StringUtils.isEmpty(limit)){
            limit=5;
        }

        IPage<CrmBanner> iPage=crmBannerService.getBannerListForKey(current,limit,crmBannerQuery);

        List<CrmBanner> list=null;
        long total = 0;
        if (iPage!=null){
            list=iPage.getRecords();
            total=  iPage.getTotal();
        }
        return list==null?Msg.fail().message("图片数据为空"):Msg.success().data("list",list).data("total",total);
    }

    //插入一条数据
    @ApiOperation("插入一个banner")
    @PostMapping("/insertBanner")
    public Msg insertBanner(
            @ApiParam(value = "插入的实体数据",name ="crmBanner" )
            @RequestBody @Valid CrmBanner crmBanner, BindingResult bindingResult){

        Msg msg = ErrorUtil.getError(bindingResult, "map", "数据不规范!");
        if (!msg.getSuccess()){
            return msg;
        }
        boolean save = crmBannerService.save(crmBanner);
        if (!save){
            return Msg.fail().message("插入失败");
        }
        return Msg.success();
    }

    //修改一条数据
    @ApiOperation("修改banner")
    @PostMapping("/updateBanner")
    public Msg updateBanner(
            @ApiParam(value = "修改的实体数据",name ="crmBanner" )
            @RequestBody @Valid CrmBanner crmBanner, BindingResult bindingResult){
        Msg msg = ErrorUtil.getError(bindingResult, "map", "数据不规范!");
        if (!msg.getSuccess()){
            return msg;
        }
        crmBannerService.updateBanner(crmBanner);
        return Msg.success();
    }

    //删除一条数据
    @ApiOperation("删除banner")
    @DeleteMapping("/delBanner/{id}")
    public Msg delBanner(@ApiParam(value = "banner的id",name = "id")@PathVariable(required = false)String id){
        boolean b = crmBannerService.removeById(id);
        return b?Msg.success():Msg.fail().message("删除失败");
    }

}

