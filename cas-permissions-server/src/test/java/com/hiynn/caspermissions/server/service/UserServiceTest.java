package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserByIdTest() {
        Long userId = 1L;
        User user = userService.getUserById(userId);
        System.out.println(user);
    }

    @Test
    public void getUserByUsername() {
        String username = "admin";
        User user = userService.getUserByUsername(username);
        System.out.println(user);
    }
}
