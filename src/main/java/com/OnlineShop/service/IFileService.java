package com.OnlineShop.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2017/12/8 0008.
 */
public interface IFileService {
    public String upload(MultipartFile file, String path);
}
