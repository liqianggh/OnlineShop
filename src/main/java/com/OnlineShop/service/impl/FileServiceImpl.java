package com.OnlineShop.service.impl;

import com.OnlineShop.service.IFileService;
import com.OnlineShop.util.FTPUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/8 0008.
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl  implements IFileService{

//    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);


    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);

        //对名字进行重置
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        log.info("开始上传文件，上传文件名:{},上传路径:{},新文件名{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功了
           boolean result =  FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到服务器
log.info("文件上传结果？？？"+result);
            //上传成功后删除本地文件
            if(result){
                targetFile.delete();
            }
        }catch (IOException e) {
            log.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }

}
