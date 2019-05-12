package cn.itsource.aigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author zt
 * @version V1.0
 * @className ZuulGatewayApplication
 * @description 网关服务类
 * @date 2019/5/11 21:30
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient //可以不写，表明是Eureka客户端
public class ZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }
}
