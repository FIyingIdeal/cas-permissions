package com.hiynn.caspermissions.server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author yanchao
 * @date 2017/12/8 9:52
 */
public class Resource implements Serializable {

    private Long id;
    private Long appId;
    private String name;
    private String url;
    private String roleIds;
    private Set<String> roles;
    private String permissionIds;
    private Set<String> permissions;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Resource() {}

    public Resource(Long appId, String name, String url, String roleIds, String permissionIds,
                    LocalDateTime gmtCreate, LocalDateTime gmtModified) {
        this.appId = appId;
        this.name = name;
        this.url = url;
        this.roleIds = roleIds;
        this.permissionIds = permissionIds;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", appId=" + appId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", roleIds='" + roleIds + '\'' +
                ", roles=" + roles +
                ", permissionIds='" + permissionIds + '\'' +
                ", permissions=" + permissions +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
