package com.hiynn.caspermissions.core.shiro;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import com.hiynn.caspermissions.core.util.CasPermissionUtils;
import io.buji.pac4j.realm.Pac4jRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author yanchao
 * @date 2017/12/11 14:28
 */
public class HiynnCasPermissionPac4jRealm extends Pac4jRealm {

    private static final Logger logger = LoggerFactory.getLogger(HiynnCasPermissionPac4jRealm.class);

    @Resource(name = "remoteService")
    private IRemoteService remoteService;
    @Value("${server.service.appKey}")
    private String appKey;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return super.doGetAuthenticationInfo(authenticationToken);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = CasPermissionUtils.getUsername(principals);
        final Set<String> roles = remoteService.getUserAppRoles(username, appKey);
        final Set<String> permissions = remoteService.getUserAppPermissions(username, appKey);
        logger.debug("用户【{}】在系统【{}】中的权限信息：roles={}, permissions={}", username, appKey, roles, permissions);

        final SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roles);
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

}
