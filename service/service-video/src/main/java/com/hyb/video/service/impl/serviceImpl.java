package com.hyb.video.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.hyb.CommonUtil.JwtUtils;
import com.hyb.CommonUtil.RedisUtils;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.video.service.VideoService;
import com.hyb.video.util.DefaultClient;
import com.hyb.video.util.VideoUtil;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class serviceImpl implements VideoService {


    @Override
    public String upVideoFile(MultipartFile file) throws Exception {


        String title=System.currentTimeMillis()+"_"+ UUID.randomUUID().toString();

        String fileName=file.getOriginalFilename();

        InputStream inputStream=file.getInputStream();

        UploadStreamRequest request = new UploadStreamRequest(VideoUtil.accessKey, VideoUtil.accesskeyValue, title, fileName, inputStream);

        //视频分类区域
        request.setCateId(Long.valueOf(VideoUtil.cateId));

        /* 模板组ID(可选) */
        request.setTemplateGroupId(VideoUtil.templateGroupId);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        //请求视频点播服务的请求ID
        String videoId;
        if (response.isSuccess()) {
            videoId=response.getVideoId();
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            videoId=response.getVideoId();
        }
        return videoId;
    }

    @Override
    public void delVideoFile(String id) throws Exception {
        DefaultAcsClient client = DefaultClient.initVodClient(VideoUtil.accessKey, VideoUtil.accesskeyValue);

        DeleteVideoRequest request = new DeleteVideoRequest();

        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(id);
        try{
            client.getAcsResponse(request);
        }catch (Exception e){
            throw new HybException(20001,"视频不存在");
        }
    }

    @Override
    public void delVideoFileByBatch(List<String> list) throws Exception {

        DefaultAcsClient client = DefaultClient.initVodClient(VideoUtil.accessKey, VideoUtil.accesskeyValue);

        DeleteVideoRequest request = new DeleteVideoRequest();

        //支持传入多个视频ID，多个用逗号分隔
        //这里的StringUtil用Apache的
        String ids = StringUtil.join(list.toArray(), ",");

        request.setVideoIds(ids);
        try{
            client.getAcsResponse(request);
        }catch (Exception e){
            throw new HybException(20001,"视频不存在");
        }
    }

    /*获取播放凭证函数*/
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client,String id) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(id);
        return client.getAcsResponse(request);
    }


    @Override
    public String getPlayAuth(String id, HttpServletRequest request) {

        DefaultAcsClient client = null;
        GetVideoPlayAuthResponse response =null;
        String auth=null;
        try {
            response=new GetVideoPlayAuthResponse();
            client = DefaultClient.initVodClient("LTAI5tPhJowYKeDWZZRXatRJ",
                    "GVctkqOloR0Ur25Q9qp8UMF9oFcUwm");
            response = getVideoPlayAuth(client,id);
            //播放凭证
            auth = response.getPlayAuth();

        } catch (Exception e) {
            throw new HybException(20001,"未知异常,视频可能不存在");
        }
        //这里调试的过程中,被调用了两次,我也不知道为什么
        if (request.getHeader("token")!=null){
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
//        每个会员id,播放同一个视频一次才被记录,且只被记录一次
            RedisUtils.setHash("videoCount",memberId+id,"1",26, TimeUnit.HOURS);
        }
       return auth;
    }

    /*获取播放地址函数*/
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client,String id) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(id);
        return client.getAcsResponse(request);
    }

    @Override
    public List<String> getPlayAddress(String id,HttpServletRequest request) {
        DefaultAcsClient client = null;
        GetPlayInfoResponse response = null;
        List<String> list=new ArrayList<>();
        try {
            client = DefaultClient.initVodClient("LTAI5tPhJowYKeDWZZRXatRJ",
                    "GVctkqOloR0Ur25Q9qp8UMF9oFcUwm");
            response = getPlayInfo(client,id);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                String playURL = playInfo.getPlayURL();
                list.add(playURL);
            }

        } catch (Exception e) {
            throw new HybException(20001,"未知异常,视频可能不存在");
        }
//        String memberId = JwtUtils.getMemberIdByJwtToken(request);
//        //每个会员id,播放同一个视频一次才被记录,且只被记录一次
//        RedisUtils.setHash("videoCount",memberId+id,"1",26, TimeUnit.HOURS);

        return list;
    }
}
