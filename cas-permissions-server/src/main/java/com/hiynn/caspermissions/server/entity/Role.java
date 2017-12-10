package com.hiynn.caspermissions.server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yanchao
 * @datetime 2017-12-7 21:43:40
 */
public class Role implements Serializable {

    private Long id;
    private Long appId;
    private String role;
    private String roleName;
    private Boolean available = Boolean.TRUE;
    private String description;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Role() {}

    public Role(Long appId, String role, String roleName, Boolean available, String description,
                LocalDateTime gmtCreate, LocalDateTime gmtModified) {
        this.appId = appId;
        this.role = role;
        this.roleName = roleName;
        this.available = available;
        this.description = description;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Role{" +
                "id=" + id +
                ", appId=" + appId +
                ", role='" + role + '\'' +
                ", roleName='" + roleName + '\'' +
                ", available=" + available +
                ", description='" + description + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
