package com.hiynn.caspermissions.server.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yanchao
 * @date 2017/11/21 16:44
 */
@RestController
@RequestMapping(value = "/service")
public class HelloController {

    @Value("${server.testHelloMessage}")
    private String testHelloMessage;

    /**
     * 如果是线上环境的话，应该是构造通知前端登录成功的JSON
     * @return
     */
    @RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.POST})
    public String helloWorld() {
        return "Hello " + testHelloMessage + "!";
    }

    @GetMapping(value = "/secondRequest")
    public String secondRequest() {
        return "Second Request!";
    }

    @GetMapping(value = "/login")
    public String login(@RequestParam Map<String, String> userInfo) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(
                userInfo.get("username"), userInfo.get("password"));
        //使用CotrollerAdvice来处理异常，这里只是测试
        try {
            subject.login(token);
            return "login success";
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return "login fail";
    }

}
