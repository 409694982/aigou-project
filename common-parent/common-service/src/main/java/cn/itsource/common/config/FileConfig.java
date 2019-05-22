package cn.itsource.common.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

/**
 * @author zt
 * @version V1.0
 * @className FileConfig
 * @description TODO
 * @date 2019/5/20 10:10
 */
@SpringBootConfiguration
public class FileConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(1024L * 1024L);
        return factory.createMultipartConfig();
    }
}
