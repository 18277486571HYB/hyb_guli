package com.hyb.servicecount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.servicecount.entity.StatisticsDaily;
import com.hyb.servicecount.mapper.StatisticsDailyMapper;
import com.hyb.servicecount.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-03-20
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Override
    public Map<String, Object> getCharts(String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        List<StatisticsDaily> list = this.list(wrapper);
        List<Integer> registerList=new ArrayList<>();
        List<Integer> loginList=new ArrayList<>();
        List<Integer> videoList=new ArrayList<>();
        List<Integer> courseList=new ArrayList<>();
        List<String> date=new ArrayList<>();
        for (StatisticsDaily s :
                list) {
            registerList.add(s.getRegisterNum());
            loginList.add(s.getLoginNum());
            videoList.add(s.getVideoViewNum());
            courseList.add(s.getCourseNum());
            date.add(s.getDateCalculated());
        }
        Map<String,Object> map=new HashMap<>();
        map.put("registerList",registerList);
        map.put("loginList",loginList);
        map.put("videoList",videoList);
        map.put("courseList",courseList);
        map.put("dateList",date);
        return map;
    }
}
