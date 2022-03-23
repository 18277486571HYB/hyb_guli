package com.hyb.servicecount.service;

import com.hyb.servicecount.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-03-20
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    Map<String, Object> getCharts(String begin, String end);
}
