package com.shan.controller;

import com.shan.utils.QiniuUtils;
import com.shan.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        // 原始图片名称[可能会出现重复覆盖，修改成随机名称]
        String originalFilename = file.getOriginalFilename();
        // 唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");

        /**
         * 上传文件上传到哪呢？七牛云云服务器按量付费，速度快，把图片发放到离用户最近的服务器上
         * 降低我们自身应用服务器的带宽消耗
         */
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}
