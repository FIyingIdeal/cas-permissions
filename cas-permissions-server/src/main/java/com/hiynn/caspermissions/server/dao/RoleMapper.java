package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select({"select id, app_id, role, role_name, available, description, gmt_create, gmt_modified from t_role " +
            "where id = #{roleId}"})
    Role getRoleById(Long roleId);

    @Select({"select id, app_id, role, role_name, available, description, gmt_create, gmt_modified from t_role " +
            "where app_id = #{appId}"})
    List<Role> getRolesByAppId(Long appId);

}
