package com.hiynn.caspermissions.core.remote;

import java.util.Map;
import java.util.Set;

/**
 * @author yanchao
 * @datetime 2017-12-10 15:39:49
 */
public interface IRemoteService {

    Set<String> getUserAppRoles(String username, String appKey);

    Set<String> getUserAppRoles(Long userId, String appKey);

    Set<String> getUserAppPermissions(String username, String appKey);

    Set<String> getUserAppPermissions(Long userId, String appKey);

    Map<String, String> getAppFilterChainDefinitionMap(String appKey);
}
