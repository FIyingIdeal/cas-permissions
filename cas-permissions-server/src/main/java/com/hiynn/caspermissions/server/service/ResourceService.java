package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.ResourceMapper;
import com.hiynn.caspermissions.server.entity.Application;
import com.hiynn.caspermissions.server.entity.Permission;
import com.hiynn.caspermissions.server.entity.Resource;
import com.hiynn.caspermissions.server.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
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
    public static final String UPDATE_DYNAMIC_PERMISSIONS_URI = "/client/updateDynamicPermission";

    public static final String ROLE_PERMISSION_IDS_SEPARATOR = ",";
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 新增单条Resource
     * @param resource
     * @return
     */
    public int insertResource(Resource resource) {
         int insertCount = resourceMapper.insertResource(resource);
         if (insertCount > 0) {
             updateApplicationDynamicPermissions(resource.getAppId());
         }
         return insertCount;
    }

    /**
     * 新增多条Resource，由于不同数据库对一次插入多条数据支持方式不同，故采用一条一条插入方式
     * @param resources
     * @return
     */
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
    public int updateResource(Resource resource) {
        resource.setGmtModified(LocalDateTime.now());
        int updateCount = resourceMapper.updateResource(resource);
        if (updateCount > 0) {
            updateApplicationDynamicPermissions(resource.getAppId());
        }
        return updateCount;
    }

    public int deleteResource(Long id) {
        Resource resource = resourceMapper.getResourceById(id);
        int deleteCount = resourceMapper.deleteResource(id);
        if (deleteCount > 0) {
            updateApplicationDynamicPermissions(resource.getAppId());
        }
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

    /**
     * 动态权限刷新
     * @param appId
     */
    private void updateApplicationDynamicPermissions(Long appId) {
        Application application = applicationService.getApplicationById(appId);
        String url = generateUpdateDynamicPermissionUrl(application);
        logger.debug("更新系统权限地址：{}", url);
        try {
            String result = restTemplate.getForObject(url, String.class);
            logger.info("权限更新结果：{} {}", url, result);
        } catch (HttpClientErrorException e) {
            logger.warn("系统权限刷新地址【{}】错误，请更正后重试！", url);
        }
    }

    private String generateUpdateDynamicPermissionUrl(Application application) {
        String appName = application.getAppName();
        String schema = application.getAppSchema();
        String domain = application.getAppDomain();
        int port = application.getAppPort();
        String contextPath = application.getAppContextPath();
        if (!StringUtils.hasText(schema)) {
            schema = "http";
            logger.info("无法获取系统【{}】的Schema值，设置默认为{}", appName, schema);
        }
        if (!StringUtils.hasText(domain) || !StringUtils.hasText(contextPath)) {
            logger.warn("无法获取系统【{}】Domain与ContextPath值，请检查配置是否正确！", appName);
            //TODO 自定义异常
            throw new NullPointerException("请求Domain（IP或域名）与ContextPath不允许为空【无ContextPath请指定为'/'】");
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(schema).append("://")
                .append(domain).append(":")
                .append(port).append(contextPath)
                .append(UPDATE_DYNAMIC_PERMISSIONS_URI);
        return builder.toString();
    }
}
