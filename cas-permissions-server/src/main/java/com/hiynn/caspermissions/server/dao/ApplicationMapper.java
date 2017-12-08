package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.Application;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ApplicationMapper {

    @Insert({"insert into t_app (app_key, app_name, app_desc, app_status, app_context_path, gmt_create, gmt_modified) " +
            "values (#{appKey}, #{appName}, #{appDesc}, #{appStatus}, #{appContextPath}, #{gmtCreate}, #{gmtModified})"})
    int insertApplication(Application application);

    @Update({"update t_app set app_key = #{appKey}, app_name = #{appName}, app_desc = #{appDesc}, app_status = #{appStatus}," +
            " app_context_path = #{appContextPath}, gmt_modified = #{gmtModified} where id = #{id} "})
    int updateApplication(Application application);

    @Delete({"delete from t_app where id = #{id}"})
    int deleteApplication(Long id);

    @Select({"select id from t_app where app_key = #{appKey}"})
    Long getAppIdByAppKey(String appKey);
}
