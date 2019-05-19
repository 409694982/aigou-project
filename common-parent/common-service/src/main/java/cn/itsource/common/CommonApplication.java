package cn.itsource.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author zt
 * @version V1.0
 * @className CommonApplication
 * @description 公共的启动类
 * @date 2019/5/17 16:56
 */
@SpringBootApplication
@EnableEurekaClient
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }
}
