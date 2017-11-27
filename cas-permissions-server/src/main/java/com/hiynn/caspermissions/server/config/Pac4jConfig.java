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

    @Value("${shiro.cas.callbackUrl}")
    private String callbackUrl;

    @Value("${shiro.cas.casClientName}")
    private String casClientName;

    /*@Bean
    public Config config() {
        CasConfiguration configuration = new CasConfiguration(
                casServerLoginUrl, casServerUrlPrefix);
        configuration.setAcceptAnyProxy(true);
        CasClient client = new CasClient(configuration);
        String callbackUrl = callbackUrl + "?client_name=" + casClientName;
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
        CasConfiguration casConfig = new CasConfiguration(casServerLoginUrl);
        casConfig.setLogoutHandler(casLogoutHandler);
        //TODO 不同的设置不同的毛病......
        CasClient casClient = new CasClient(casConfig);
        casClient.setConfiguration(casConfig);
        //设置client的name属性，注意要与callbackUrl中的client_name值一致，否则的话会报错：找不到 XXX client
        casClient.setName(casClientName);
        Clients clients = new Clients();
        /**
         * 如果在callbackUrl中不添加client_name请求参数，会导致ticket验证通不过 -.-!! 整整困扰了我一周时间...
         * TODO 该问题应该是和获取TGT的时候所使用的url不一致导致的，如果不自己添加client_name或设置了defaultClient的话，不知道什么时候会添加这个请求参数，具体需要研究代码
         */
        clients.setCallbackUrl(callbackUrl);
        clients.setClients(casClient);
        Config config = new Config(clients);
        return config;
    }

}
