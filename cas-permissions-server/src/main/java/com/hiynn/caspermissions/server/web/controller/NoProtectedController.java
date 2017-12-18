package com.hiynn.caspermissions.server.web.controller;

import com.hiynn.caspermissions.server.service.RemoteServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanchao
 * @date 2017/12/1 14:21
 */
@RestController
public class NoProtectedController {

    @Autowired
    private RemoteServerService remoteService;

    @GetMapping(value = "/hello1")
    public String test() {
        return "Hello World!";
    }

    @GetMapping(value = "/ajaxlogout")
    public String logout() {
        return "bye, logout success!";
    }

    @GetMapping(value = "/remote")
    public Object getRemote() {
        return remoteService.getUserAppRoles("admin", "appkey1");
    }
}
