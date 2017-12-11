package com.hiynn.caspermissions.core.util;

import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.pac4j.core.profile.CommonProfile;

/**
 * @author yanchao
 * @date 2017/12/11 11:00
 */
public class CasPermissionUtils {

    public static CommonProfile getCommonProfile() {
        final PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        if (principals != null) {
            Pac4jPrincipal principal = principals.oneByType(Pac4jPrincipal.class);
            if (principal != null) {
                return principal.getProfile();
            }
        }
        return null;
    }

    public static String getUsername() {
        CommonProfile commonProfile = getCommonProfile();
        if (commonProfile != null) {
            //TODO CAS返回信息配置不全，暂时使用id保存了username，待完善
            return commonProfile.getId();
        }
        return null;
    }

    public static String getUsername(PrincipalCollection principals) {
        final Pac4jPrincipal principal = principals.oneByType(Pac4jPrincipal.class);
        if (principal != null) {
            CommonProfile commonProfile = principal.getProfile();
            return commonProfile.getId();
        }
        return null;
    }
}
