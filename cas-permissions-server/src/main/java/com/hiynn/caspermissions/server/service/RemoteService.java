package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import com.hiynn.caspermissions.server.entity.Resource;
import com.hiynn.caspermissions.server.entity.User;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author yanchao
 * @date 2017/12/10 15:39
 */
@Service
public class RemoteService implements IRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(RemoteService.class);

    public static final String ROLE_PATTERN_STRING = "roles[{0}]";
    public static final String PERMISSION_PATTERN_STRING = "perms[{0}]";

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Value("${shiro.cas.enabled}")
    private boolean shiroPac4jCasEnabled;

    @Override
    public Set<String> getUserAppRoles(String username, String appKey) {
        return getUserAppRoles(getUserIdByUsername(username), appKey);
    }

    @Override
    public Set<String> getUserAppRoles(Long userId, String appKey) {
        return roleService.getUserAppStringRoles(userId, getAppIdByAppKey(appKey));
    }

    @Override
    public Set<String> getUserAppPermissions(String username, String appKey) {
        return getUserAppPermissions(getUserIdByUsername(username), appKey);
    }

    @Override
    public Set<String> getUserAppPermissions(Long userId, String appKey) {
        return roleService.getUserAppStringPermissions(userId, getAppIdByAppKey(appKey));
    }

    @Override
    public Map<String, String> getAppFilterChainDefinitionMap(String appKey) {
        Long appId = getAppIdByAppKey(appKey);
        List<Resource> resources = resourceService.getResourcesByAppId(appId);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        for (Resource resource : resources) {
            Set<String> roles = resource.getRoles();
            Set<String> permissions = resource.getPermissions();
            String filters = combineRolesAndPerms(roles, permissions);
            //TODO 使用shiro-pac4j进行单点登录的话，需要使用securityFilter来对资源进行过滤，这个过滤器代替了authc。所有要进行权限验证的动作前必须先登录！！！
            if (shiroPac4jCasEnabled) {
                filters = "securityFilter," + filters;
            }
            filterChainDefinitionMap.put(resource.getUrl(), filters);
        }
        return filterChainDefinitionMap;
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

    private String combineRolesAndPerms(Set<String> roles, Set<String> perms) {
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(roles)) {
            sb.append(ROLE_PATTERN_STRING.replace("{0}", String.join(",", roles)));
        }
        if (!CollectionUtils.isEmpty(perms)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(PERMISSION_PATTERN_STRING.replace("{0}", String.join(",", perms)));
        }
        return sb.toString();
    }
}
