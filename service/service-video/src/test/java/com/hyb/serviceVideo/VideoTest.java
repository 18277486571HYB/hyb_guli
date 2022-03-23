package com.hyb.serviceVideo;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VideoTest {
    /*获取播放地址函数*/
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("95e716ac5b1840b7a48ec8cd6cf51863");
        return client.getAcsResponse(request);
    }

    /*获取播放凭证函数*/
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId("95e716ac5b1840b7a48ec8cd6cf51863");
        return client.getAcsResponse(request);
    }

    public static GetVideoInfoResponse getVideoInfo(DefaultAcsClient client) throws Exception {
        GetVideoInfoRequest request = new GetVideoInfoRequest();
        request.setVideoId("95e716ac5b1840b7a48ec8cd6cf51863");
        return client.getAcsResponse(request);
    }

    @Test
    public void getVideoInfo() throws ClientException {
        DefaultAcsClient client =DefaultClient.initVodClient("LTAI5tPhJowYKeDWZZRXatRJ",
                "GVctkqOloR0Ur25Q9qp8UMF9oFcUwm");
        GetVideoInfoResponse response = new GetVideoInfoResponse();
        try {
            response = getVideoInfo(client);
            List<GetVideoInfoResponse.Video.Thumbnail> thumbnailList = response.getVideo().getThumbnailList();
            System.out.println(thumbnailList);
            System.out.print("Title = " + response.getVideo().getTitle() + "\n");
            System.out.print("Description = " + response.getVideo().getDescription() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    @Test
    public void getVideoAddress(){
        DefaultAcsClient client = null;
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        try {
            client = DefaultClient.initVodClient("LTAI5tPhJowYKeDWZZRXatRJ",
                    "GVctkqOloR0Ur25Q9qp8UMF9oFcUwm");
            response = getPlayInfo(client);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    @Test
    public void getVideoId(){
        DefaultAcsClient client = null;
        GetVideoPlayAuthResponse response =null;
        try {
            response=new GetVideoPlayAuthResponse();
            client = DefaultClient.initVodClient("LTAI5tPhJowYKeDWZZRXatRJ",
                    "GVctkqOloR0Ur25Q9qp8UMF9oFcUwm");
            response = getVideoPlayAuth(client);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        if (response!=null){
            System.out.print("RequestId = " + response.getRequestId() + "\n");
        }
    }

    @Test
    public void upLoadFile(){
        String accessKeyId="LTAI5tPhJowYKeDWZZRXatRJ";
        String accessKeySecret="GVctkqOloR0Ur25Q9qp8UMF9oFcUwm";
        String title="新上传视频1"; //上传后的名字
        String fileName="E:\\javaStudy\\StudyingProject\\6 - What If I Want to Move Faster.mp4";
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
}
