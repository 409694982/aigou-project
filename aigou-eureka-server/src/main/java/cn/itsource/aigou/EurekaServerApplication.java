package cn.itsource.aigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zt
 * @version V1.0
 * @className EurekaServerApplication
 * @description Eureka服务端
 * @date 2019/5/11 21:22
 */
@SpringBootApplication
@EnableEurekaServer //表示是Eureka服务端
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
