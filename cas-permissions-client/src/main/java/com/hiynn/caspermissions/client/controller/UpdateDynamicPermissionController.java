package com.hiynn.caspermissions.client.controller;

import com.hiynn.caspermissions.core.service.DynamicPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanchao
 * @date 2017/12/18 18:00
 */
@RestController
@RequestMapping(value = "/client")
public class UpdateDynamicPermissionController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDynamicPermissionController.class);

    @Autowired
    private DynamicPermissionService permissionService;
    @Value("${server.service.appKey}")
    private String appKey;
    @Value("${shiro.loadDBPermissionsEnabled}")
    private boolean loadDBPermissionsEnabled;

    @RequestMapping(value = "/updateDynamicPermission")
    public String updateApplicationDynamicPermissions() {
        if (!loadDBPermissionsEnabled) {
            logger.info("系统动态权限获取未开启，权限更新动作无效");
            return "权限更新无效";
        }
        logger.info("系统【{}】动态权限更新开始...", appKey);
        permissionService.reloadPermission();
        logger.info("系统【{}】动态权限更新完成...", appKey);
        return "权限更新完成";
    }
}
