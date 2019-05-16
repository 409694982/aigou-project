package cn.itsource.aigou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zt
 * @version V1.0
 * @className ProductApplication
 * @description 商品服务的启动类
 * @date 2019/5/16 19:12
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@MapperScan("cn.itsource.aigou.mapper")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
