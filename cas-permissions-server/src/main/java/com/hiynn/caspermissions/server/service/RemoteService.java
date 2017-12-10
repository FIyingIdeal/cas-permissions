package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import com.hiynn.caspermissions.server.entity.User;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * @author yanchao
 * @date 2017/12/10 15:39
 */
@Service
public class RemoteService implements IRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(RemoteService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private RoleService roleService;

    @Override
    public Set<String> getRoles(String username, String appKey) {
        return getRoles(getUserIdByUsername(username), appKey);
    }

    @Override
    public Set<String> getRoles(Long userId, String appKey) {
        return roleService.getUserAppStringRoles(userId, getAppIdByAppKey(appKey));
    }

    @Override
    public Set<String> getPermissions(String username, String appKey) {
        return getPermissions(getUserIdByUsername(username), appKey);
    }

    @Override
    public Set<String> getPermissions(Long userId, String appKey) {
        return roleService.getUserAppStringPermissions(userId, getAppIdByAppKey(appKey));
    }

    private Long getUserIdByUsername(String username) {
        User user = userService.getUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            //TODO 处理异常
            throw new UnknownAccountException();
        }
        return user.getId();
    }

    private Long getAppIdByAppKey(String appKey) {
        Long appId = applicationService.getAppIdByAppKey(appKey);
        if (ObjectUtils.isEmpty(appId)) {
            //TODO 抛出自定义异常并处理
            //throw new
        }
        return appId;
    }
}
