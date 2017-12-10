package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.entity.Permission;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author yanchao
 * @datetime 2017-12-10 11:44:36
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PermissionServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceTest.class);

    @Autowired
    private PermissionService permissionService;

    @Test
    public void getPermissionsByAppId() {
        Long appId = 1L;
        List<Permission> permissions = permissionService.getPermissionsByAppId(appId);
        logger.info("permissions info : " + permissions);
    }
}
