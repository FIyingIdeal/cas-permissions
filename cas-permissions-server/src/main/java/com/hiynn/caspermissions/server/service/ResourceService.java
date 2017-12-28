package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.ResourceMapper;
import com.hiynn.caspermissions.server.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
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

    /**
     * 新增单条Resource，该接口由{@link ResourceService#insertResources(List)}替代并对外提供服务
     * 如果一次插入多条数据的话，需要将系统权限的刷新放置在insertResources(List)中
     * 如果放置在insertResource()中的话，由于多次rest请求的顺序无法保证而可能导致系统权限异常
     * @param resource
     * @return
     */
    private int insertResource(Resource resource) {
         int insertCount = resourceMapper.insertResource(resource);
         return insertCount;
    }

    /**
     * 新增多条Resource，由于不同数据库对一次插入多条数据支持方式不同，故采用一条一条插入方式
     * @param resources
     * @return
     */
    @Transactional
    public int insertResources(List<Resource> resources) {
        int insertCount = 0;
        for (Resource resource : resources) {
            insertCount += insertResource(resource);
        }
        return insertCount;
    }

    /**
     * 更改Resource，注意id与appId不允许修改
     * @param resource
     * @return
     */
    @Transactional
    public int updateResource(Resource resource) {
        resource.setGmtModified(LocalDateTime.now());
        int updateCount = resourceMapper.updateResource(resource);
        return updateCount;
    }

    @Transactional
    public int deleteResource(Long id) {
        int deleteCount = resourceMapper.deleteResource(id);
        return deleteCount;
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
