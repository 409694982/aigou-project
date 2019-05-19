package cn.itsource.common.controller;

import cn.itsource.common.domain.ModelAndPath;
import cn.itsource.common.util.VelocityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zt
 * @version V1.0
 * @className GenetareStaticPage
 * @description 生成静态页面
 * @date 2019/5/17 19:42
 */
@RestController
public class GenerateStaticPage {

    @PostMapping("/page")
    public void generateStaticPage(@RequestBody ModelAndPath modelAndPath){
        VelocityUtils.staticByTemplate(modelAndPath.getModel(), modelAndPath.getTemplatePath(), modelAndPath.getTargetPath());
    }
}
