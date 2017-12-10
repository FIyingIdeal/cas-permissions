package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.RoleMapper;
import com.hiynn.caspermissions.server.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanchao
 * @datetime 2017-12-10 10:41:03
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

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
}
