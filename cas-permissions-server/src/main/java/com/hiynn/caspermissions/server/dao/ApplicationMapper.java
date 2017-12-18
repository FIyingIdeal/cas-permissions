package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.Application;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ApplicationMapper {

    @Insert({"insert into t_app (app_key, app_name, app_desc, app_status, app_schema, app_domain, app_port, app_context_path, gmt_create, gmt_modified) " +
            "values (#{appKey}, #{appName}, #{appDesc}, #{appStatus}, #{appSchema}, #{appDomain}, #{appPort}, #{appContextPath}, #{gmtCreate}, #{gmtModified})"})
    int insertApplication(Application application);

    @Update({"update t_app set app_key = #{appKey}, app_name = #{appName}, app_desc = #{appDesc}, app_status = #{appStatus}," +
            " app_schema = #{appSchema}, app_domain = #{appDomain}, app_port = #{appPort}, app_context_path = #{appContextPath}," +
            " gmt_modified = #{gmtModified} where id = #{id} "})
    int updateApplication(Application application);

    @Delete({"delete from t_app where id = #{id}"})
    int deleteApplication(Long id);

    @Select({"select id, app_key as appKey, app_name as appName, app_desc as appDesc, app_status as appStatus," +
            " app_schema as appSchema, app_domain as appDomain, app_port as appPort, app_context_path as appContextPath," +
            " gmt_create as gmtCreate, gmt_modified as gmtModified from t_app where id = #{id}"})
    Application getApplicationById(Long id);

    @Select({"select id, app_key as appKey, app_name as appName, app_desc as appDesc, app_status as appStatus," +
            " app_schema as appSchema, app_domain as appDomain, app_port as appPort, app_context_path as appContextPath," +
            " gmt_create as gmtCreate, gmt_modified as gmtModified from t_app where app_key = #{appKey}"})
    Application getApplicationByAppKey(String appKey);

    @Select({"select id from t_app where app_key = #{appKey}"})
    Long getAppIdByAppKey(String appKey);
}
