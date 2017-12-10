package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @author yanchao
 * @datetime 2017-12-10 10:34:32
 */
@Mapper
public interface RoleMapper {

    @Insert({"insert into t_role (app_id, role, role_name, available, description, gmt_create, gmt_modified) values " +
            "(#{appId}, #{role}, #{roleName}, #{available}, #{description}, #{gmtCreate}, #{gmtModified})"})
    int insertRole(Role role);


    @Delete({"delete from t_role where id = #{id}"})
    int deleteRole(Long id);

    @Select({"select id, app_id as appId, role, role_name as roleName, available, description, " +
            "gmt_create as gmtCreate, gmt_modified as gmtModified from t_role where id = #{roleId}"})
    Role getRoleById(Long roleId);

    @Select({"select id, app_id as appId, role, role_name as roleName, available, description, " +
            "gmt_create as gmtCreate, gmt_modified as gmtModified from t_role where app_id = #{appId}"})
    List<Role> getRolesByAppId(Long appId);

    @Select({"<script>" +
            "select permission from t_permission where id in " +
                "(select permission_id from t_role_permission where role_id in " +
                    "<foreach item='item' index='index' collection='roleIds' open='(' separator=',' close=')'>#{item}</foreach>" +
                ")" +
            "</script>"})
    Set<String> getRolePermissions(@Param("roleIds") Set<Long> roleIds);

    @Select({"select id, app_id as appId, role, role_name as roleName, available, description, gmt_create as gmtCreate, " +
            "gmt_modified as gmtModified from t_role where id in " +
            "(select role_id from t_user_role where user_id = #{userId}) and app_id = #{appId}"})
    Set<Role> getUserAppRoles(@Param("userId") Long userId, @Param("appId") Long appId);

}
