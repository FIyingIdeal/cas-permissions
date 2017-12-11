package com.hiynn.caspermissions.client.service;

import com.hiynn.caspermissions.core.remote.IRemoteService;
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
public class IRemoteServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(IRemoteServiceTest.class);

    @Resource(name = "remoteService")
    private IRemoteService remoteService;

    @Test
    public void getRoles() {
        String username = "admin";
        String appKey = "appkey1";
        logger.info("user app roles : " + remoteService.getRoles(username, appKey));
    }
}
