package com.hiynn.caspermissions.server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yanchao
 * @date 2017/12/8 9:35
 */
public class Application implements Serializable {

    private Long id;
    private String appKey;
    private String appName;
    private String appDesc;
    private Boolean appStatus = Boolean.TRUE;
    private String appSchema;
    private String appDomain;
    private int appPort;
    private String appContextPath;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Application() {}

    public Application(String appKey, String appName, String appDesc, Boolean appStatus, String appSchema,
                       String appDomain, int appPort, String appContextPath, LocalDateTime gmtCreate, LocalDateTime gmtModified) {
        this.appKey = appKey;
        this.appName = appName;
        this.appDesc = appDesc;
        this.appStatus = appStatus;
        this.appSchema = appSchema;
        this.appDomain = appDomain;
        this.appPort = appPort;
        this.appContextPath = appContextPath;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Application(Long id, String appKey, String appName, String appDesc, Boolean appStatus, String appSchema,
                       String appDomain, int appPort, String appContextPath, LocalDateTime gmtCreate, LocalDateTime gmtModified) {
        this.id = id;
        this.appKey = appKey;
        this.appName = appName;
        this.appDesc = appDesc;
        this.appStatus = appStatus;
        this.appSchema = appSchema;
        this.appDomain = appDomain;
        this.appPort = appPort;
        this.appContextPath = appContextPath;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public Boolean getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Boolean appStatus) {
        this.appStatus = appStatus;
    }

    public String getAppSchema() {
        return appSchema;
    }

    public void setAppSchema(String appSchema) {
        this.appSchema = appSchema;
    }

    public String getAppDomain() {
        return appDomain;
    }

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }

    public int getAppPort() {
        return appPort;
    }

    public void setAppPort(int appPort) {
        this.appPort = appPort;
    }

    public String getAppContextPath() {
        return appContextPath;
    }

    public void setAppContextPath(String appContextPath) {
        this.appContextPath = appContextPath;
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
        return "Application{" +
                "id=" + id +
                ", appKey='" + appKey + '\'' +
                ", appName='" + appName + '\'' +
                ", appDesc='" + appDesc + '\'' +
                ", appStatus=" + appStatus +
                ", appSchema='" + appSchema + '\'' +
                ", appDomain='" + appDomain + '\'' +
                ", appPort=" + appPort +
                ", appContextPath='" + appContextPath + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
