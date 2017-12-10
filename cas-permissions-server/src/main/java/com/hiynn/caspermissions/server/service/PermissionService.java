package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.PermissionMapper;
import com.hiynn.caspermissions.server.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanchao
 * @datetime 2017-12-10 11:39:41
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public int insertPermission(Permission permission) {
        return permissionMapper.insertPermission(permission);
    }

    public int deletePermission(Long id) {
        return permissionMapper.deletePermission(id);
    }

    public Permission getPermissionById(Long permissionId) {
        return permissionMapper.getPermissionById(permissionId);
    }

    public List<Permission> getPermissionsByAppId(Long appId) {
        return permissionMapper.getPermissionsByAppId(appId);
    }
}
