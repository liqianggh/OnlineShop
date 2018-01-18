package com.mmall.test;

import com.OnlineShop.util.FTPUtil;
import com.OnlineShop.util.PropertiesUtil;
import com.google.common.collect.Lists;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/24 0024.
 */
public class FtpUtilsTest {

    public static void main(String[]args) throws  Exception{
        FTPClient ftpClient = new FTPClient();
            ftpClient.connect("localhost");
            boolean isSuccess = ftpClient.login("liqiang","liqiang");
            System.out.println(isSuccess);

            ftpClient.changeWorkingDirectory("localhost");
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            //二进制文件
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            FileInputStream    fis = new FileInputStream("E:\\PhotoFromInternet\\timg.jpg");
                boolean flag = ftpClient.storeFile("123",fis);
        System.out.println(flag+"sdsfsda");


    }


}
