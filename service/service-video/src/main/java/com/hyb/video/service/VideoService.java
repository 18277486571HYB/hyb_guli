package com.hyb.video.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface VideoService {

    String upVideoFile(MultipartFile file) throws IOException, Exception;

    void delVideoFile(String id) throws ClientException, Exception;

    void delVideoFileByBatch(List<String> list) throws Exception;

    String getPlayAuth(String id, HttpServletRequest request);

    List<String> getPlayAddress(String id,HttpServletRequest request);
}
