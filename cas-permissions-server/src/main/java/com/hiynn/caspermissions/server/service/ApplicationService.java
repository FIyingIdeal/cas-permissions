package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.ApplicationMapper;
import com.hiynn.caspermissions.server.entity.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanchao
 * @date 2017/12/8 10:37
 */
@Service
public class ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    public int addApplication(Application application) {
        return applicationMapper.insertApplication(application);
    }

    public int updateApplication(Application application) {
        return applicationMapper.updateApplication(application);
    }

    public int deleteApplication(Long id) {
        return applicationMapper.deleteApplication(id);
    }

    public Long getAppIdByAppKey(String appKey) {
        return applicationMapper.getAppIdByAppKey(appKey);
    }
}
