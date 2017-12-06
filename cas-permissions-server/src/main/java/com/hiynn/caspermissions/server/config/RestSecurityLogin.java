package com.hiynn.caspermissions.server.config;

import org.pac4j.core.client.Client;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.engine.DefaultSecurityLogic;

import java.util.List;

/**
 * @author yanchao
 * @date 2017/11/30 11:49
 */
@Deprecated
public class RestSecurityLogin extends DefaultSecurityLogic<Object, J2EContext> {

    @Override
    protected boolean loadProfilesFromSession(J2EContext context, List<Client> currentClients) {
        return true;
    }
}
