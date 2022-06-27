package com.hyb.serviceedu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.*;
import com.hyb.serviceedu.entity.vo.EduCoursePublishQuery;
import com.hyb.serviceedu.entity.vo.EduCourseQuery;
import com.hyb.serviceedu.mapper.EduCourseMapper;
import com.hyb.serviceedu.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    EduChapterService eduChapterService;

    @Autowired
    EduVideoService eduVideoService;

    @Autowired
    EduTeacherService eduTeacherService;

    @Autowired
    EduSubjectService eduSubjectService;

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public EduCourse saveCourse(EduCourseQuery eduCourseQuery) {
//        如果教师id不存在
        EduTeacher eduTeacher = this.getEduTeacher(eduCourseQuery.getTeacherId());
        if (eduTeacher==null){
            return null;
        }
//        如果课程分类id不存在
        EduSubject eduSubject = this.getEduSubject(eduCourseQuery.getSubjectId());
        if (eduSubject==null){
            return null;
        }


        EduCourse eduCourse = new EduCourse();

        BeanUtils.copyProperties(eduCourseQuery,eduCourse);

//        设置课程父级id
//        eduCourse.setSubjectParentId(eduSubject.getParentId());

        boolean course = this.save(eduCourse);

        if (!course){
            return null;
        }

        EduCourseDescription eduCourseDescription = new EduCourseDescription();

        eduCourseDescription.setId(eduCourse.getId());

        eduCourseDescription.setDescription(eduCourseQuery.getDescription());

        boolean description = eduCourseDescriptionService.save(eduCourseDescription);

        return description?eduCourse:null;
    }

    @Override
    public EduCourseQuery getCourseById(String courseId) {
        EduCourse eduCourse = this.getById(courseId);
        if (eduCourse==null){
            return null;

        }
        EduCourseQuery eduCourseQuery = new EduCourseQuery();
        BeanUtils.copyProperties(eduCourse,eduCourseQuery);
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        if (eduCourseDescription!=null){
            eduCourseQuery.setDescription(eduCourseDescription.getDescription());
        }
        return eduCourseQuery;
    }

    @Override
    public int updateCourse(EduCourseQuery eduCourseQuery) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(eduCourseQuery,eduCourse);
        boolean b = this.updateById(eduCourse);
        if (!b){
            return 0;
        }
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescription.setDescription(eduCourseQuery.getDescription());
        boolean update = eduCourseDescriptionService.updateById(eduCourseDescription);
        return update?1:0;
    }

    @Override
    public EduCoursePublishQuery getPublishCourseInfo(String id) {
        return baseMapper.getPublishCourseInfo(id);
    }

    @Override
    public void delCourse(String id) {
        try{
            //删除小节
            eduVideoService.delVideoByCourseId(id);

            //删除章节
            eduChapterService.delChapterByCourseId(id);
            //删除描述
            eduCourseDescriptionService.removeById(id);
            //删除课程本身
            this.removeById(id);
        }catch (HybException hybException){
            throw new HybException(20001, hybException.getMsg());
        }
    }

    @Override
    public IPage<EduCourse> getCourseForKey(Integer current, Integer limit, EduCourse eduCourse) {

        if(StringUtils.isEmpty(current)){
            current=1;
        }
        if (StringUtils.isEmpty(limit)){
            limit=8;
        }


        Page<EduCourse> page = new Page<>(current, limit);

        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();

        String title = eduCourse.getTitle();
        BigDecimal price = eduCourse.getPrice();
        String teacherId = eduCourse.getTeacherId();
        String subjectId = eduCourse.getSubjectId();
        String subjectParentId = eduCourse.getSubjectParentId();
        Long buyCount = eduCourse.getBuyCount();
        Long viewCount = eduCourse.getViewCount();
        Integer lessonNum = eduCourse.getLessonNum();
        Date gmtCreate = eduCourse.getGmtCreate();
        Date gmtModified = eduCourse.getGmtModified();
        if (!StringUtils.isEmpty(title)){
            eduCourseQueryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(price)){
            eduCourseQueryWrapper.like("price",price);
            eduCourseQueryWrapper.orderByAsc("price");
        }
        if (!StringUtils.isEmpty(teacherId)){
            eduCourseQueryWrapper.eq("teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            eduCourseQueryWrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            eduCourseQueryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(buyCount)){
            eduCourseQueryWrapper.ge("buy_count",buyCount);
            eduCourseQueryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(viewCount)){
            eduCourseQueryWrapper.ge("view_count",viewCount);
            eduCourseQueryWrapper.orderByDesc("view_count");
        }
        if (!StringUtils.isEmpty(lessonNum)){
            eduCourseQueryWrapper.ge("lesson_num",lessonNum);
            eduCourseQueryWrapper.orderByAsc("lesson_num");
        }
        if (!StringUtils.isEmpty(gmtCreate)){
            eduCourseQueryWrapper.ge("gmt_create",gmtCreate);
            eduCourseQueryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(gmtModified)){
            eduCourseQueryWrapper.le("gmt_modified",gmtModified);
        }

        return this.page(page, eduCourseQueryWrapper);
    }

    @Override
    public List<EduCoursePublishQuery> getCourseByTeacherId(String id) {
        return baseMapper.getPublishCourseInfoByTeacherId(id);
    }


    /*
    * 1. 空结果缓存: 解决缓存穿透问题
    * 2. 随机的过期时间: 解决缓存雪崩问题
    * 3. 加锁: 解锁缓存击穿问题
    * */

    @Override
    public List<EduCourse> listCourse(Integer limit) {
        return redisLock(limit);
    }

    private List<EduCourse> list(Integer limit){
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        eduCourseQueryWrapper.orderByDesc("view_count","buy_count");
        eduCourseQueryWrapper.last("limit "+limit);
        return this.list(eduCourseQueryWrapper);
    }

    private List<EduCourse> redisLock(Integer limit) {

        String frontCourse = redisTemplate.opsForValue().get("frontCourse");
        //占分布式锁
        //设置UUID防止被别人删除
        String uuid = UUID.randomUUID().toString();
        //setIfAbsent 将设置过期时间和加锁这两个操作是原子操作
        //防止有删除锁的线程阻塞,导致该线程锁过期,其他线程获取锁,但是线程不阻塞后会误删其他锁
        if (frontCourse!=null){
            return JSON.parseObject(frontCourse,new TypeReference<List<EduCourse>>(){});
        }
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3000, TimeUnit.MILLISECONDS);
        if (Boolean.TRUE.equals(lock)){
            //加上try的原因是防止redisList过慢导致先删除了key
            List<EduCourse> eduCourses;
            try{
                eduCourses = redisList(limit);
            }finally {
                //第一种方案:
                //执行成功后要解锁
                //为了防止别的线程删除自己,所以做比较
                //但是这样在比较过程可能会出现网络延迟,还是会出现删除别人锁的情况
                //所以一定要做成原子操作
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if (Objects.equals(lockValue, uuid)){
//                redisTemplate.delete(uuid);
//            }
                //第二种方案,使用lua脚本实现
                String s="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

                redisTemplate.execute(new DefaultRedisScript<>(s,Integer.class),Arrays.asList(uuid),uuid);

            }
            return eduCourses;
        }else {
            //自旋获取锁
            return redisLock(limit);
        }

    }

    private List<EduCourse> redisList(Integer limit){
        String frontCourse = redisTemplate.opsForValue().get("frontCourse");

        if (frontCourse==null){

            List<EduCourse> list = list(limit);

            Random random = new Random();

            redisTemplate.opsForValue().set("frontCourse", String.valueOf(list),random.nextInt(100)+200, TimeUnit.HOURS);

            return list;
        }

        return JSON.parseObject(frontCourse,new TypeReference<List<EduCourse>>(){});

    }

    private List<EduCourse> synchronizedLock(Integer limit) {
        synchronized (this){
            QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
            eduCourseQueryWrapper.orderByDesc("view_count","buy_count");
            eduCourseQueryWrapper.last("limit "+limit);
            return this.list(eduCourseQueryWrapper);
        }
    }


    //    教师id是否存在
    private EduTeacher getEduTeacher(String id){
        return eduTeacherService.getById(id);
    }

//    课程分类id是否存在
    private EduSubject getEduSubject(String id){
        return eduSubjectService.getById(id);
    }
}
