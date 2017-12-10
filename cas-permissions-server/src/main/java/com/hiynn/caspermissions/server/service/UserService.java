package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.UserMapper;
import com.hiynn.caspermissions.server.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanchao
 * @date 2017/12/8 11:10
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

     public int insertUser(User user) {
         return userMapper.insertUser(user);
     }

     public int updateUser(User user) {
         return userMapper.updateUser(user);
     }

     public int deleteUser(Long id) {
         return userMapper.deleteUser(id);
     }

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
