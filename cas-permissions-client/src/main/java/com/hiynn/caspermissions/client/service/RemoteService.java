package com.hiynn.caspermissions.client.service;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author yanchao
 * @date 2017/12/10 19:32
 */
@Service
public class RemoteService {

    @Autowired
    private IRemoteService iRemoteService;

    public Set<String> getRoles(String username, String appKey) {
        return iRemoteService.getRoles(username, appKey);
    }
}
