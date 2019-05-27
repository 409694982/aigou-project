package cn.itsource.common.client;

import cn.itsource.aigou.util.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("COMMON")
public interface FileClient {

    /**
     * 上传
     * @param file
     * @return
     */
    @PostMapping("/file/upload")
    AjaxResult upload(@RequestParam("file") MultipartFile file);

    /**
     * 删除
     * @param fileId
     * @return
     */
    @GetMapping("/file/delete")
    AjaxResult delete(@RequestParam("fileId") String fileId);
}
