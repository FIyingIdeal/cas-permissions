package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.Permission;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @author yanchao
 * @datetiem 2017-12-10 11:06:19
 */
@Mapper
public interface PermissionMapper {

    @Insert({"insert into t_permission (app_id, permission, permission_name, available, description, " +
            "gmt_create, gmt_modified) values (appId, permission, permissionName, available, description," +
            "gmtCreate, gmtModified)"})
    int insertPermission(Permission permission);

    @Delete({"delete from t_permission where id = #{id}"})
    int deletePermission(Long id);

    @Select({"select id, app_id as appId, permission, permission_name as permissionName, available, description, " +
            "gmt_create as gmtCreate, gmt_modified as gmtModified from t_permission where id = #{permissionId}"})
    Permission getPermissionById(Long permissionId);

    @Select({"<script>" +
            "select id, app_id as appId, permission, permission_name as permissionName, available, description, " +
                "gmt_create as gmtCreate, gmt_modified as gmtModified from t_permission where id in " +
                    "<foreach item='item' index='index' collection='permissionIds' open='(' separator=',' close=')'>#{item}</foreach>" +
            "</script>"})
    List<Permission> getPermissionsByIds(@Param("permissionIds") Set<Long> permissionIds);

    @Select({"select id, app_id as appId, permission, permission_name as permissionName, available, description, " +
            "gmt_create as gmtCreate, gmt_modified as gmtModified from t_permission where app_id = #{appId}"})
    List<Permission> getPermissionsByAppId(Long appId);
}
