package com.hyb.servicecms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyb.servicecms.entity.CrmBanner;
import com.hyb.servicecms.entity.vo.CrmBannerQuery;
import com.hyb.servicecms.mapper.CrmBannerMapper;
import com.hyb.servicecms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-03-01
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {


    @Override
    public IPage<CrmBanner> getBannerListForKey(Integer current, Integer limit, CrmBannerQuery crmBannerQuery) {
        Page<CrmBanner> crmBannerPage = new Page<>(current, limit);
        QueryWrapper<CrmBanner> crmBannerQueryWrapper = new QueryWrapper<>();
        String id = crmBannerQuery.getId();
        String title = crmBannerQuery.getTitle();
        String sort = crmBannerQuery.getSort();
        Date gmtCreate = crmBannerQuery.getGmtCreate();
        Date gmtModified = crmBannerQuery.getGmtModified();
        if (!StringUtils.isEmpty(id)){
            crmBannerQueryWrapper.like("id",id);
        }
        if (!StringUtils.isEmpty(title)){
            crmBannerQueryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(sort)){
            crmBannerQueryWrapper.ge("sort",sort);
        }
        if (!StringUtils.isEmpty(gmtCreate)){
            crmBannerQueryWrapper.ge("gmt_create",gmtCreate);
        }
        if (!StringUtils.isEmpty(gmtModified)){
            crmBannerQueryWrapper.le("gmt_modified",gmtModified);
        }
        crmBannerQueryWrapper.orderByDesc("gmt_create");
        return this.page(crmBannerPage,crmBannerQueryWrapper);
    }

    @Override
    @Cacheable(key = "'getList'",value = "banner")
    public List<CrmBanner> getList(QueryWrapper<CrmBanner> crmBannerQueryWrapper) {
        return this.list(crmBannerQueryWrapper);
    }

    //@CachePut支持双写,一般用于插入
    //写入数据库同时将返回的数据缓存

    //将banner分区下,key为getList的缓存删除
//    @Caching(
//            evict = {
//                    @CacheEvict(key = "'getList'",value = "banner")
//            }
//    )
    @Override
    //将banner分区所有缓存删除
    @CacheEvict(value = "'banner'",allEntries = true)
    public void updateBanner(CrmBanner crmBanner) {
        this.updateById(crmBanner);
    }
}
