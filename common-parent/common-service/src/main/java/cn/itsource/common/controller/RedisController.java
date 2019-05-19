package cn.itsource.common.controller;

import cn.itsource.common.util.RedisUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zt
 * @version V1.0
 * @className RedisController
 * @description Redis缓存接口
 * @date 2019/5/17 18:59
 */
@RestController
public class RedisController {

    @PostMapping("/redis")
    public void set(@RequestParam("key") String key, @RequestParam("value") String value){
        RedisUtils.INSTANCE.set(key, value);
    }

    @GetMapping("/redis")
    public String get(@RequestParam("key") String key){
        return RedisUtils.INSTANCE.get(key);
    }
}
