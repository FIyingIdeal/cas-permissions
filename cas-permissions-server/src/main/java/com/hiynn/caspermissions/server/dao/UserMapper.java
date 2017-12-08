package com.hiynn.caspermissions.server.dao;

import com.hiynn.caspermissions.server.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yanchao
 * @date 2017/12/8 11:10
 */
@Mapper
public interface UserMapper {

    @Insert({"insert into t_user (username, password, salt, locked, organization_id, email, telephone," +
            "birthday, gmt_create, gmt_modified) values (#{username}, #{password}, #{salt}, #{locked}, #{organizationId}," +
            " #{email}, #{telephone}, #{birthday}, #{gmtCreate}, #{gmtModified})"})
    int insertUser(User user);

}
