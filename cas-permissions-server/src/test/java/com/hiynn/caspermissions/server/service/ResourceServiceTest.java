package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.entity.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yanchao
 * @date 2017/12/10 13:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ResourceServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceTest.class);

    @Autowired
    private ResourceService resourceService;

    @Test
    public void insertResourceTest() {
        LocalDateTime now = LocalDateTime.now();
        Resource resource = new Resource(1L, "testPermission", "/testPermission", "1,2", "2,3", now, now);
        int insertCount = resourceService.insertResource(resource);
        logger.info("insert " + insertCount + " resource(s) success!");
    }

    @Test
    public void deleteResourceTest() {
        Long id = 8L;
        int deleteCount = resourceService.deleteResource(id);
        logger.info("delete " + deleteCount + " resource(s) success!");
    }

    @Test
    public void getResourceById() {
        Long id = 9L;
        Resource resource = resourceService.getResourceById(id);
        logger.info("resource info : " + resource);
    }

    @Test
    public void getResourcesByAppId() {
        Long appId = 1L;
        List<Resource> resources = resourceService.getResourcesByAppId(appId);
        logger.info("resources info : " + resources);
    }
}
