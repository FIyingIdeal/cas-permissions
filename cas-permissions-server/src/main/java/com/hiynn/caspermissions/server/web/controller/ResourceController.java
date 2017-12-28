package com.hiynn.caspermissions.server.web.controller;

import com.hiynn.caspermissions.server.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanchao
 * @date 2017/12/26 15:26
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    //@DeleteMapping("/deleteResource/{appId}")
    @GetMapping("/deleteResource/{appId}")//测试
    public Object deleteResource(@PathVariable("appId") Long appId) {
        return resourceService.deleteResource(appId);
    }
}
