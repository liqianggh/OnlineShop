package com.OnlineShop.util;


import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 */
public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");

    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");

    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");


    private String ip;
    private String user;
    private int port;
    private String password;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String password) {
        this.ip = ip;
        this.user = user;
        this.port = port;
        this.password = password;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始连接服务器");
        logger.info(fileList.size()+"哈哈哈");
        boolean result = ftpUtil.uploadFile("img",fileList);
        logger.info("开始链接ftp服务器，结束上传，上传结果{}",result);
        return result;
    }

    /**
     * 上传文件的具体逻辑
     * @param remotePath
     * @param fileList
     * @return
     */
    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = false;
        FileInputStream fis = null;

        //连接ftp服务器
        if(connectFtpServer(this.ip,this.port,this.user,this.password)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                //二进制文件
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    logger.info(fileItem.getName()+" "+fis.getChannel());
                    //todo
                    ftpClient.enterLocalPassiveMode();
                    logger.info("端口"+ftpClient.getPassivePort());
                    logger.info(ftpClient.getPassiveHost());
                    logger.info(String.valueOf(ftpClient.getRemoteAddress()));
                    logger.info(String.valueOf(ftpClient.getRemotePort()));
                    uploaded = ftpClient.storeFile(fileItem.getName(),fis);

                }
            } catch (IOException e) {
                logger.error("上传文件异常",e);
                uploaded=false;
                e.printStackTrace();
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }
    private boolean connectFtpServer(String ip,int port,String user,String password){
        ftpClient = new FTPClient();
        boolean isSuccess = false;
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,password);
            logger.info("连接文件服务器"+isSuccess);
        } catch (IOException e) {
            logger.error("链接ftp服务器异常",e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
