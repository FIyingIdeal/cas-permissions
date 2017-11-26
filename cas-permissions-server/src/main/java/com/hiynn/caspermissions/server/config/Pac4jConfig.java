package com.hiynn.caspermissions.server.config;

import com.hiynn.caspermissions.server.shiro.ShiroCasLogoutHandler;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
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

    @Value("${shiro.cas.casService}")
    private String casService;

    @Value("${shiro.cas.casClientName}")
    private String casClientName;

    /*@Bean
    public Config config() {
        CasConfiguration configuration = new CasConfiguration(
                casServerLoginUrl, casServerUrlPrefix);
        configuration.setAcceptAnyProxy(true);
        CasClient client = new CasClient(configuration);
        String callbackUrl = casService + "?client_name=" + casClientName;
        client.setCallbackUrl(callbackUrl);
        client.setName(casClientName);
        Clients clients = new Clients(callbackUrl, client);
        //clients.setDefaultClient(client);
        Config config = new Config(clients);

        return config;
    }*/

    @Bean
    public Config config() {
        ShiroCasLogoutHandler casLogoutHandler = new ShiroCasLogoutHandler();
        CasConfiguration casConfig = new CasConfiguration();
        casConfig.setLoginUrl(casServerLoginUrl);
        casConfig.setLogoutHandler(casLogoutHandler);
        CasClient casClient = new CasClient();
        casClient.setConfiguration(casConfig);
        Clients clients = new Clients();
        clients.setCallbackUrl(casService);
        clients.setClients(casClient);
        clients.setDefaultClient(casClient);
        Config config = new Config(clients);
        return config;
    }

}
