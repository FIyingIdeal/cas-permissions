package com.hiynn.caspermissions.server.service;

import com.hiynn.caspermissions.server.dao.ResourceMapper;
import com.hiynn.caspermissions.server.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanchao
 * @date 2017/12/10 13:54
 */
@Service
public class ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    public int insertResource(Resource resource) {
        return resourceMapper.insertResource(resource);
    }

    public int deleteResource(Long id) {
        return resourceMapper.deleteResource(id);
    }

    public Resource getResourceById(Long resourceId) {
        return resourceMapper.getResourceById(resourceId);
    }

    public List<Resource> getResourcesByAppId(Long appId) {
        return resourceMapper.getResourcesByAppId(appId);
    }
}
