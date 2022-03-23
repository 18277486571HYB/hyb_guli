package com.hyb.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OssService {
    String upAvatarFile(MultipartFile multipartFile) throws Exception;

}
