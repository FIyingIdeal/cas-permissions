package com.hiynn.caspermissions.client.shiro;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import com.hiynn.caspermissions.core.util.CasPermissionUtils;
import io.buji.pac4j.realm.Pac4jRealm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author yanchao
 * @date 2017/12/11 14:28
 */
public class ClientPac4jRealm extends Pac4jRealm {

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
        final Set<String> roles = remoteService.getRoles(username, appKey);
        final Set<String> permissions = remoteService.getPermissions(username, appKey);
        final SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roles);
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

}
