package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.entity.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * @author yanchao
 * @date 2017/12/8 10:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;

    @Test
    public void insertApplicationTest() {
        Application application = new Application("appKey", "test", "test Desc", Boolean.TRUE, "http",
                "192.168.3.20", 8080, "/test", LocalDateTime.now(), LocalDateTime.now());
        int addCount = applicationService.addApplication(application);
        System.out.println("Add " + addCount + " Application success!");
    }

    @Test
    public void updateApplicationTest() {
        Application application = new Application(2L, "appKeyUpdate", "test", "test Desc", Boolean.TRUE, "http",
                "192.168.3.20", 8080, "/test", LocalDateTime.now(), LocalDateTime.now());
        int updateCount = applicationService.updateApplication(application);
        System.out.println("update " + updateCount + " Application success!");
    }

    @Test
    public void deleteApplicationTest() {
        Long id = 2L;
        int deleteCount = applicationService.deleteApplication(id);
        System.out.println("Delete " + deleteCount + " Application success!");
    }

    @Test
    public void getAppIdByAppKey() {
        String appKey = "appkey1";
        System.out.println("The appId is " + applicationService.getAppIdByAppKey(appKey));
    }
}
