package com.hiynn.caspermissions.core.remote;

import java.util.Set;

/**
 * @author yanchao
 * @datetime 2017-12-10 15:39:49
 */
public interface IRemoteService {

    Set<String> getRoles(String username, String appKey);

    Set<String> getRoles(Long userId, String appKey);

    Set<String> getPermissions(String username, String appKey);

    Set<String> getPermissions(Long userId, String appKey);
}
