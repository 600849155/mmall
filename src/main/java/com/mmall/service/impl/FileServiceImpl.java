package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * Created by Administrator on 2018-1-23.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);//扩展名abc.jpg那就读到.jpg
        String uploadFilename = UUID.randomUUID()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名：{}，上传的路径：{},新文件名:{}",fileName,path,uploadFilename);

        File fileDir = new File(path);//绝对路径
        if (!fileDir.exists()){
            fileDir.setWritable(true);//设置此抽象路径名的所有者的写权限
            fileDir.mkdirs();//创建此抽象路径名指定的目录，包括创建必需但不存在的父目录。

        }
        File targetFile = new File(path,uploadFilename);

        try{
            file.transferTo(targetFile);//将上传文件写到服务器上指定的文件。 springmvc
            //文件上传成功
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //  将TargetFile上传到FTP服务器上
            targetFile.delete();
            //  上传完之后，删除upload下面的文件
        } catch (IOException e) {
            logger.error("上传文件异常",e);
         return null;
        }

        return targetFile.getName();
    }
}
