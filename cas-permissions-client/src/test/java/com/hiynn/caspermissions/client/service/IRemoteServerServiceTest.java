package com.hiynn.caspermissions.client.service;

import com.hiynn.caspermissions.core.remote.IRemoteServerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author yanchao
 * @date 2017/12/10 19:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IRemoteServerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(IRemoteServerServiceTest.class);

    @Resource(name = "remoteService")
    private IRemoteServerService remoteService;

    @Test
    public void getRoles() {
        String username = "admin";
        String appKey = "appkey1";
        logger.info("user app roles : " + remoteService.getUserAppRoles(username, appKey));
    }
}
