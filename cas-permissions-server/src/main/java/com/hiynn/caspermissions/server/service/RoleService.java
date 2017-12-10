package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.RoleMapper;
import com.hiynn.caspermissions.server.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanchao
 * @datetime 2017-12-10 10:41:03
 */
@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public static final String USER_APP_SEPARATOR = "$";

    @Autowired
    private RoleMapper roleMapper;

    //缓存user对应的Role，可以使用Cache来代替
    private Map<String, Set<Role>> userAppRoleCache = new HashMap<>();

    public int insertRole(Role role) {
        return roleMapper.insertRole(role);
    }

    public int deleteRole(Long id) {
        return roleMapper.deleteRole(id);
    }

    public Role getRoleById(Long roleId) {
        return roleMapper.getRoleById(roleId);
    }

    public List<Role> getRolesByAppId(Long appId) {
        return roleMapper.getRolesByAppId(appId);
    }

    public Set<String> getRolePermissions(Set<Long> roleIds) {
        return roleMapper.getRolePermissions(roleIds);
    }

    public Set<Role> getUserAppRoles(Long userId, Long appId) {
        Set<Role> roles = getUserAppRoleFromCache(userId, appId);
        if (CollectionUtils.isEmpty(roles)) {
            logger.info("从数据库中获取用户（{}）系统（{}）角色信息", userId, appId);
            roles = roleMapper.getUserAppRoles(userId, appId);
            userAppRoleCache.put(getUserAppRoleCacheKey(userId, appId), roles);
        } else {
            logger.info("从缓存中获取用户（{}）系统（{}）角色信息", userId, appId);
        }
        return roles;
    }

    public Set<String> getUserAppStringRoles(Long userId, Long appId) {
        Set<Role> roles = getUserAppRoles(userId, appId);
        return roles.stream().map(Role::getRole).collect(Collectors.toSet());
    }

    public Set<String> getUserAppStringPermissions(Long userId, Long appId) {
        Set<Role> roles = getUserAppRoles(userId, appId);
        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        return getRolePermissions(roleIds);
    }

    private String getUserAppRoleCacheKey(Long userId, Long appId) {
        return userId + USER_APP_SEPARATOR + appId;
    }

    private Set<Role> getUserAppRoleFromCache(Long userId, Long appId) {
        return userAppRoleCache.get(getUserAppRoleCacheKey(userId, appId));
    }
}
