package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RoleServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceTest.class);

    @Autowired
    private RoleService roleService;

    @Test
    public void insertRoleTest() {
        Role role = new Role(1L, "testRole", "testRole", Boolean.TRUE, "testRole desc",
                LocalDateTime.now(), LocalDateTime.now());
        int insertCount = roleService.insertRole(role);
        logger.info("insert " + insertCount + " role(s) success!");
    }

    @Test
    public void deleteRoleTest() {
        Long id = 4L;
        int deleteCount = roleService.deleteRole(id);
        logger.info("delete " + deleteCount + " role(s) success!");
    }

    @Test
    public void getRoleById() {
        Long id = 3L;
        Role role = roleService.getRoleById(id);
        logger.info("role info : " + role);
    }

    @Test
    public void getRolesByAppId() {
        Long appId = 1L;
        List<Role> roles = roleService.getRolesByAppId(appId);
        logger.info("roles info : " + roles);
    }

    @Test
    public void getPermissions() {
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L,2L));
        Set<String> permissions = roleService.getRolePermissions(roleIds);
        System.out.println(permissions);
    }
}
