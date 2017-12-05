package com.hiynn.caspermissions.server.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanchao
 * @date 2017/12/1 14:21
 */
@RestController
public class NoProtectedController {

    @GetMapping(value = "/hello1")
    public String test() {
        return "Hello World!";
    }

    @GetMapping(value = "/ajaxlogout")
    public String logout() {
        return "bye, logout success!";
    }
}
