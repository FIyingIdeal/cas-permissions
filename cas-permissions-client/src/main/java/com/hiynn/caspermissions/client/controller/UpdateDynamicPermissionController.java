package com.hiynn.caspermissions.client.controller;

import com.hiynn.caspermissions.core.entity.HiynnResponseEntity;
import com.hiynn.caspermissions.core.service.DynamicPermissionService;
import com.hiynn.caspermissions.core.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public HiynnResponseEntity updateApplicationDynamicPermissions(@RequestHeader("Authentication") String token) {
        HttpStatus httpStatus;
        String message;
        if (!checkAuthenticated(token)) {
            message = "未授权用户禁止刷新系统权限";
            httpStatus = HttpStatus.UNAUTHORIZED;
            logger.warn(message);
        } else if (!loadDBPermissionsEnabled) {
            message = "系统{}动态权限获取未开启，无需执行系统权限更新";
            httpStatus = HttpStatus.OK;
            logger.info(message, appKey);
        } else {
            message = "系统【{}】动态权限更新...";
            httpStatus = HttpStatus.OK;
            logger.info(message, appKey);
            permissionService.reloadPermission();
        }
        return new HiynnResponseEntity.Builder()
                .httpStatus(httpStatus).message(message.replace("{}", appKey)).build();
    }

    private boolean checkAuthenticated(String token) {
        if (token == null || "".equals(token)) {
            return false;
        }
        try {
            Claims claims = JwtUtils.parseJWT(token, appKey);
            String username = claims.get("username").toString();
            String appId = claims.get("appId").toString();
            boolean usernameCondition = !ObjectUtils.isEmpty(username);
            boolean appIdCondition = !ObjectUtils.isEmpty(appId);
            logger.info("用户{}对系统{}发起动态权限更新操作！", username, appKey);
            return usernameCondition && appIdCondition;
        } catch (Exception e) {
            logger.warn("系统动态权限更新发生异常：{}", e.getMessage());
            return false;
        }
    }
}
