package com.hiynn.caspermissions.server.shiro;

import io.buji.pac4j.profile.ShiroProfileManager;
import org.pac4j.cas.logout.DefaultCasLogoutHandler;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.store.Store;

/**
 * @author yanchao
 * @datetime 2017年11月26日22:31:40
 * @param <C>
 */
public class ShiroCasLogoutHandler<C extends WebContext> extends DefaultCasLogoutHandler<C> {

    public ShiroCasLogoutHandler() {}

    public ShiroCasLogoutHandler(final Store<String, Object> store) {
        super(store);
    }

    protected void destory(final C context, final SessionStore sessionStore, final String channel) {
        // remove profiles
        final ShiroProfileManager manager = new ShiroProfileManager(context);
        manager.logout();
        logger.debug("destroy the user profiles");
        // and optionally the web session
        if (isDestroySession()) {
            logger.debug("destroy the whole session");
            final boolean invalidated = sessionStore.destroySession(context);
            if (!invalidated) {
                logger.error("The session has not been invalidated for {} channel logout", channel);
            }
        }
    }
}
