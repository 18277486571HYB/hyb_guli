package com.hyb.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.client.VideoClient;
import com.hyb.serviceedu.entity.EduChapter;
import com.hyb.serviceedu.entity.EduVideo;
import com.hyb.serviceedu.mapper.EduVideoMapper;
import com.hyb.serviceedu.service.EduChapterService;
import com.hyb.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    EduChapterService eduChapterService;

    @Autowired
    VideoClient videoClient;



    @Override
    public void addVideo(EduVideo eduVideo) {
        EduChapter chapter = this.getChapter(eduVideo.getChapterId());
        if (chapter==null){
            throw new HybException(20001,"章节不存在");
        }
        if (!eduVideo.getCourseId().equals(chapter.getCourseId())){
            throw new HybException(20001,"章节与课程不对应");
        }
        boolean save = this.save(eduVideo);
        if (!save){
            throw new HybException(20001,"未知异常");
        }
    }

    @Override
    public void delVideo(String id) {
        //根据小节id获取小节,然后获取视频id
        EduVideo video = this.getVideById(id);
        String videoSourceId = video.getVideoSourceId();
        if (videoSourceId!=null){
            videoClient.delVideo(videoSourceId);
        }

        boolean b = this.removeById(id);
        if (!b){
            throw new HybException(20001,"删除失败");
        }
    }

    @Override
    public void updateVideo(EduVideo eduVideo) {
        boolean b = this.updateById(eduVideo);
        if (!b){
            throw new HybException(20001,"更新失败");
        }
    }

    @Override
    public EduVideo getVideById(String id) {

        EduVideo video = this.getById(id);
        if (video==null){
            throw new HybException(20001,"没有此小节");
        }
        return video;
    }

    @Override
    public void delVideoByCourseId(String id) {
//        先删除视频
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",id);
        //只将这个字段封装在返回的对象中
        videoQueryWrapper.select("video_source_id");
        List<EduVideo> list = this.list(videoQueryWrapper);
        //要判断集合里的对象是否为空,而不是判断对象里的字段是否为空
        //因为前面我们只将视频id封装给了返回的对象,如果视频id为空了,这个对象就一定为空
        //这个时候你根据这个对象的get方法获取视频id就会报错
        //而如果这个对象不为空,视频id就一定不为空,因为对象里只封装视频id这个属性

        //下面是流的写法
        //List<String> collect = list.stream().filter(eduVideo -> !StringUtils.isEmpty(eduVideo)).map(EduVideo::getVideoSourceId).collect(Collectors.toList());
        List<String> collect=new ArrayList<>();
        for (EduVideo e :
                list) {

            if (e!=null){
                collect.add(e.getVideoSourceId());
            }
        }
        if (collect.size()!=0){
            videoClient.delBatchVideo(collect);
        }
        videoClient.delBatchVideo(collect);

        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",id);

        boolean remove = this.remove(eduVideoQueryWrapper);
        if (!remove){
            throw new HybException(20001,"删除小节失败");
        }
    }

    //查询小节表的章节id是否存在且其章节id的所在课程id是否与小节表对应的课程id相等
    //判断章节是否存在
    private EduChapter getChapter(String id){
        return eduChapterService.getById(id);
    }
}
