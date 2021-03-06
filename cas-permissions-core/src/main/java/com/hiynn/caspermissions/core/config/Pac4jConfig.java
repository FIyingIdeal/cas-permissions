package com.hiynn.caspermissions.core.config;

import io.buji.pac4j.profile.ShiroProfileManager;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.logout.DefaultCasLogoutHandler;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.store.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanchao
 * @date 2017/11/20 20:32
 */
@Configuration
public class Pac4jConfig {

    @Value("${shiro.cas.casServerUrlPrefix}")
    private String casServerUrlPrefix;

    @Value("${shiro.cas.casServerLoginUrl}")
    private String casServerLoginUrl;

    @Value("${shiro.cas.callbackUrl}")
    private String callbackUrl;

    /**
     * 使用CAS Server登录页面的Config配置，但这个在前后端分离的时候是需要解决CORS问题的
     * @return
     */
    @Bean(name = "loginPageConfig")
    public Config loginPageConfig() {
        CasConfiguration casConfig = new CasConfiguration(casServerLoginUrl);
        casConfig.setLogoutHandler(new ShiroCasLogoutHandler());
        CasClient casClient = new CasClient(casConfig);
        /**
         * 如果手动设置client的name属性，注意要与callbackUrl中的client_name值一致，否则的话会报错：找不到 XXX client
         * 更好的方式是交给SecurityFilter来处理，它会在处理Client的时候检查callbackUrl中是否包含client_name=，如果没有的话，
         * 会自动添加。而该请求参数的值如果没有设置的话，会取实际Client（如CasClient）的SimpleName
         * {@link Clients#internalInit()}
         * {@link Clients#updateCallbackUrlOfIndirectClient(IndirectClient)}
         *
         * //casClient.setName(casClientName);
         *
         */
        Clients clients = new Clients();
        clients.setCallbackUrl(callbackUrl);
        clients.setClients(casClient);
        Config config = new Config(clients);
        return config;
    }

    /**
     * 使用REST形式的Config配置，测试中...
     * @return
     */
    @Bean(name = "restConfig")
    public Config restConfig() {
        CasConfiguration casConfiguration = new CasConfiguration(casServerLoginUrl);
        CasRestFormClient casRestClient = new CasRestFormClient(casConfiguration, "username", "password");
        Clients clients = new Clients(casRestClient);
        Config config = new Config(clients);
        return config;
    }

    static class ShiroCasLogoutHandler<C extends WebContext> extends DefaultCasLogoutHandler<C> {

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

}
