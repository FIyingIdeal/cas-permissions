package com.hiynn.caspermissions.server.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanchao
 * @date 2017/11/21 16:44
 */
@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public String helloWorld() {
        return "Hello World!";
    }
}
