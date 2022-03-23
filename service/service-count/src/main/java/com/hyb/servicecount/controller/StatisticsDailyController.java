package com.hyb.servicecount.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.CommonUtil.Msg;
import com.hyb.servicecount.entity.StatisticsDaily;
import com.hyb.servicecount.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-03-20
 */
@Api(tags = "统计controller")
@RestController
@RequestMapping("/servicecount/statistics")
@CrossOrigin
public class StatisticsDailyController {


    @Autowired
    StatisticsDailyService dailyService;

    @ApiOperation("日期查询统计记录")
    @GetMapping("/getChart/{date}")
    public Msg getChart(@PathVariable String date){
        QueryWrapper<StatisticsDaily> statisticsDailyQueryWrapper = new QueryWrapper<>();
        statisticsDailyQueryWrapper.eq("date_calculated",date);
        StatisticsDaily one = dailyService.getOne(statisticsDailyQueryWrapper);
        return one==null?Msg.fail():Msg.success().data("countData",one);
    }

    @ApiOperation("日期查询折线图")
    @GetMapping("/getCharts/{begin}/{end}")
    public Msg getCharts(@PathVariable String begin,@PathVariable String end){
        Map<String,Object> map=dailyService.getCharts(begin,end);
        return Msg.success().data("map",map);
    }
}

