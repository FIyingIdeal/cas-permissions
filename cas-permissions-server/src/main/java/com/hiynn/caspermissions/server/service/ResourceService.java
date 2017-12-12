package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.ResourceMapper;
import com.hiynn.caspermissions.server.entity.Permission;
import com.hiynn.caspermissions.server.entity.Resource;
import com.hiynn.caspermissions.server.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanchao
 * @date 2017/12/10 13:54
 */
@Service
public class ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    public static final String ROLE_PERMISSION_IDS_SEPARATOR = ",";
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    public int insertResource(Resource resource) {
        return resourceMapper.insertResource(resource);
    }

    public int deleteResource(Long id) {
        return resourceMapper.deleteResource(id);
    }

    public Resource getResourceById(Long resourceId) {
        Resource resource = resourceMapper.getResourceById(resourceId);
        if (resource != null) {
            resource.setRoles(getResourceStringRoles(resource));
            resource.setPermissions(getResourceStringPermissions(resource));
        }
        return resource;
    }

    public List<Resource> getResourcesByAppId(Long appId) {
        List<Resource> resources = resourceMapper.getResourcesByAppId(appId);
        for (Resource resource : resources) {
            resource.setRoles(getResourceStringRoles(resource));
            resource.setPermissions(getResourceStringPermissions(resource));
        }
        return resources;
    }

    public Set<String> getResourceStringRoles(Long resourceId) {
        Resource resource = getResourceById(resourceId);
        if (resource == null) {
            return Collections.EMPTY_SET;
        }
        return getResourceStringRoles(resource);
    }

    public Set<String> getResourceStringRoles(Resource resource) {
        String roleIdsStr = resource.getRoleIds();
        if (!StringUtils.hasText(roleIdsStr)) {
            return Collections.EMPTY_SET;
        }
        List<String> roleIdList = Arrays.asList(roleIdsStr.replace(" ", "").split(ResourceService.ROLE_PERMISSION_IDS_SEPARATOR));
        Set<Long> roleIds = roleIdList.stream().map(Long::parseLong).collect(Collectors.toSet());
        List<Role> roles = roleService.getRolesByIds(roleIds);
        return roles.stream().map(Role::getRole).collect(Collectors.toSet());
    }

    public Set<String> getResourceStringPermissions(Long resourceId) {
        Resource resource = getResourceById(resourceId);
        if (resource == null) {
            return Collections.EMPTY_SET;
        }
        return getResourceStringPermissions(resource);
    }

    public Set<String> getResourceStringPermissions(Resource resource) {
        String permissionIdsStr = resource.getPermissionIds();
        if (!StringUtils.hasText(permissionIdsStr)) {
            return Collections.EMPTY_SET;
        }
        List<String> permissionIdList = Arrays.asList(permissionIdsStr.replace(" ", "").split(ResourceService.ROLE_PERMISSION_IDS_SEPARATOR));
        Set<Long> permissionIds = permissionIdList.stream().map(Long::parseLong).collect(Collectors.toSet());
        List<Permission> permissions = permissionService.getPermissionsByIds(permissionIds);
        return permissions.stream().map(Permission::getPermission).collect(Collectors.toSet());
    }
}
