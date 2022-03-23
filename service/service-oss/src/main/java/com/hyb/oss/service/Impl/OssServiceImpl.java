package com.hyb.oss.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.hyb.oss.service.OssService;
import com.hyb.oss.utils.OssUtil;
import org.joda.time.DateTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String upAvatarFile(MultipartFile multipartFile) throws Exception {

        String endpoint = OssUtil.END_POINT;
        String accessKeyId = OssUtil.ACCESS_KEY_ID;
        String accessKeySecret = OssUtil.ACCESS_KEY_SECRET;
        String bucketName = OssUtil.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = multipartFile.getInputStream();

        //将文件按照时间日期进行分类
        String datePath = new DateTime().toString("yyyy/MM/dd");

        String originalFilename = datePath + "/" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replaceAll("-", "") + "_" + multipartFile.getOriginalFilename();

        ossClient.putObject(bucketName, originalFilename, inputStream);


        String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;

        inputStream.close();

        ossClient.shutdown();

        return url;
    }

}
