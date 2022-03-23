package com.hyb.servicecount.schedule;

import com.hyb.CommonUtil.RedisUtils;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.servicecount.entity.StatisticsDaily;
import com.hyb.servicecount.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class CountSchedule {

    @Autowired
    StatisticsDailyService dailyService;

    //凌晨一点
    @Scheduled(cron = "0 0 1 * * ?")
    public void insertChart(){
        long loginCount = RedisUtils.countHash("loginCount");
        long registerCount = RedisUtils.countHash("registerCount");
        long videoCount = RedisUtils.countHash("videoCount");
        long courseCount = RedisUtils.countHash("courseCount");

        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setDateCalculated(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        statisticsDaily.setLoginNum((int) loginCount);
        statisticsDaily.setRegisterNum((int) registerCount);
        statisticsDaily.setVideoViewNum((int) videoCount);
        statisticsDaily.setCourseNum((int) courseCount);
        boolean save = dailyService.save(statisticsDaily);
        if (!save){
            throw new HybException(20001,new Date()+"-统计数据失败");
        }
        if (loginCount>0){
            boolean loginCount1 = RedisUtils.delHash("loginCount");
            if (!loginCount1){
                throw new HybException(20001,new Date()+"-重置登录数节点失败");
            }
        }
        if (registerCount>0){
            boolean registerCount1 = RedisUtils.delHash("registerCount");
            if (!registerCount1){
                throw new HybException(20001,new Date()+"-重置注册数节点失败");
            }
        }

        if (videoCount>0){
            boolean videoCount1 = RedisUtils.delHash("videoCount");
            if (!videoCount1){
                throw new HybException(20001,new Date()+"重置视频数节点失败");
            }
        }

        if (courseCount>0){
            boolean courseCount1 = RedisUtils.delHash("courseCount");
            if (!courseCount1){
                throw new HybException(20001,new Date()+"重置课程数节点失败");
            }
        }

    }
}
