package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.Resource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author yanchao
 * @datetime 2017-12-10 13:46:56
 */
@Mapper
public interface ResourceMapper {

    @Insert({"insert into t_url_filter (app_id, name, url, role_ids, permission_ids, gmt_create, gmt_modified) values " +
            "(#{appId}, #{name}, #{url}, #{roleIds}, #{permissionIds}, #{gmtCreate}, #{gmtModified})"})
    int insertResource(Resource resource);

    @Delete({"delete from t_url_filter where id = #{id}"})
    int deleteResource(Long id);

    @Select({"select id, app_id as appId, name, url, role_ids as roleIds, permission_ids as permissionIds, gmt_create as gmtCreate, " +
            "gmt_modified as gmtModified from t_url_filter where id = #{resourceId}"})
    Resource getResourceById(Long resourceId);

    @Select({"select id, app_id as appId, name, url, role_ids as roleIds, permission_ids as permissionIds, gmt_create as gmtCreate, " +
            "gmt_modified as gmtModified from t_url_filter where app_id = #{appId}"})
    List<Resource> getResourcesByAppId(Long appId);

}
