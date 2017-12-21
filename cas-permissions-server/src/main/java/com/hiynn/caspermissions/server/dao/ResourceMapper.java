package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.Resource;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @author yanchao
 * @datetime 2017-12-10 13:46:56
 */
@Mapper
public interface ResourceMapper {

    @Insert({"insert into t_url_filter (app_id, name, url, role_ids, permission_ids, priority, gmt_create, gmt_modified) values " +
            "(#{appId}, #{name}, #{url}, #{roleIds}, #{permissionIds}, #{priority}, #{gmtCreate}, #{gmtModified})"})
    int insertResource(Resource resource);

    @Update({"update t_url_filter set name = #{name}, url = #{url}, role_ids = #{roleIds}, permission_ids = #{permissionIds}, " +
            "priority = #{priority}, gmt_modified = #{gmtModified} where id = #{id}"})
    int updateResource(Resource resource);

    @Delete({"delete from t_url_filter where id = #{id}"})
    int deleteResource(Long id);

    @Select({"select id, app_id as appId, name, url, role_ids as roleIds, permission_ids as permissionIds, priority," +
            " gmt_create as gmtCreate, gmt_modified as gmtModified from t_url_filter where id = #{resourceId}"})
    Resource getResourceById(Long resourceId);

    @Select({"select id, app_id as appId, name, url, role_ids as roleIds, permission_ids as permissionIds, priority," +
            " gmt_create as gmtCreate, gmt_modified as gmtModified from t_url_filter where app_id = #{appId} order by priority"})
    List<Resource> getResourcesByAppId(Long appId);

}
