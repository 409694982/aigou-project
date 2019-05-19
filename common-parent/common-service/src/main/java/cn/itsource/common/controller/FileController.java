package cn.itsource.common.controller;

import cn.itsource.aigou.util.AjaxResult;
import cn.itsource.common.util.FastDfsApiOpr;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author zt
 * @version V1.0
 * @className FileController
 * @description 文件长传和下载类
 * @date 2019/5/19 19:34
 */
@RestController
public class FileController {

    @PostMapping("/file/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file){
        try {
            //获取文件后缀名
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            //调用工具类上传并且获取到fileId
            String fileId = FastDfsApiOpr.upload(file.getBytes(), extension);
            return AjaxResult.me().setMessage("上传成功").setData(fileId);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上传失败，原因是："+e.getMessage());
        }
    }

    @GetMapping("/file/delete")
    public AjaxResult delete(@RequestParam("fileId") String fileId){
        try {
            //去掉第一个/
            String tempFile = fileId.substring(1);
            String groupName = tempFile.substring(0, tempFile.indexOf("/"));
            String fileName = tempFile.substring(tempFile.indexOf("/") + 1);
            FastDfsApiOpr.delete(groupName, fileName);
            return AjaxResult.me().setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败，原因是："+e.getMessage());
        }
    }
}
