package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Map;
import java.util.Set;

/**
 * @author yanchao
 * @date 2017/12/8 11:10
 */
@Mapper
public interface UserMapper {

    @Insert({"insert into t_user (username, password, salt, locked, organization_id, email, telephone, " +
            "birthday, gmt_create, gmt_modified) values (#{username}, #{password}, #{salt}, #{locked}, #{organizationId}," +
            " #{email}, #{telephone}, #{birthday}, #{gmtCreate}, #{gmtModified})"})
    int insertUser(User user);

    @Update({"update t_user set locked = #{locked}, organization_id = #{organizationId}, " +
            "email = #{email}, telephone = #{telephone}, birthday = #{birthday}, gmt_modified = #{gmtModified} where id = #{id}"})
    int updateUser(User user);

    @Delete({"delete from t_user where id = #{id}"})
    int deleteUser(Long id);

    @Select({"select id, username, locked, organization_id as organizationId, email, telephone, birthday, " +
            "gmt_create as gmtCreate, gmt_modified as gmtModified from t_user where id = #{id}"})
    User getUserById(Long id);

    @Select({"select id, username, locked, organization_id as organizationId, email, telephone, birthday, " +
            "gmt_create as gmtCreate, gmt_modified as gmtModified from t_user where username = #{username}"})
    User getUserByUsername(String username);

    @Select({"select password, salt from t_user where username = #{username}"})
    Map<String, String> getUserPasswordAndSaltByUsername(String username);
}
