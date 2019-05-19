package cn.itsource.common.client;

import cn.itsource.common.domain.ModelAndPath;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("COMMON")
public interface GenerateStaticPageClient {

    @PostMapping("/page")
    void generateStaticPage(@RequestBody ModelAndPath modelAndPath);
}
