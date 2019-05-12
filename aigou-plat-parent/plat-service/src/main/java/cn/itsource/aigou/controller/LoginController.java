package cn.itsource.aigou.controller;

import cn.itsource.aigou.util.AjaxResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zt
 * @version V1.0
 * @className LoginController
 * @description 登录接口
 * @date 2019/5/11 21:45
 */
@RestController
public class LoginController {

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public AjaxResult login(@RequestBody @ApiParam(name = "用户名和密码",value = "传入username和password的json格式",required = true) Map<String,String> params){
        String username = params.get("username");
        String password = params.get("password");
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return AjaxResult.me().setSuccess(false).setMessage("用户名或者密码不能为空");
        }else if ("admin".equals(username)&&"admin".equals(password)){
            return AjaxResult.me();
        }else {
            return AjaxResult.me().setSuccess(false).setMessage("用户名或者密码错误");
        }
    }
}
