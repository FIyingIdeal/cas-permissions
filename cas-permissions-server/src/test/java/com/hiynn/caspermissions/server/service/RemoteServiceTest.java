package com.hiynn.caspermissions.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yanchao
 * @date 2017/12/10 18:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RemoteServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceTest.class);

    @Autowired
    private RemoteServerService remoteService;

    @Test
    public void getRolesTest() {
        String username = "admin";
        String appKey = "appkey1";
        logger.info("user app roles : " + remoteService.getUserAppRoles(username, appKey));
    }

    @Test
    public void getPermissionsTest() {
        String username = "admin";
        String appKey = "appkey1";
        logger.info("user app permissions : " + remoteService.getUserAppPermissions(username, appKey));
    }

}
