package com.hyb.servicecms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hyb.servicecms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyb.servicecms.entity.vo.CrmBannerQuery;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-03-01
 */
public interface CrmBannerService extends IService<CrmBanner> {

    IPage<CrmBanner> getBannerListForKey(Integer current, Integer limit, CrmBannerQuery crmBannerQuery);

    List<CrmBanner> getList(QueryWrapper<CrmBanner> crmBannerQueryWrapper);

    void updateBanner(CrmBanner crmBanner);
}
