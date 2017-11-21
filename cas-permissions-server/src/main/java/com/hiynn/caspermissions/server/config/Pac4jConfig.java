package com.hiynn.caspermissions.server.config;

import io.buji.pac4j.filter.CallbackFilter;
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

    @Bean
    public Config config() {
        CasConfiguration configuration = new CasConfiguration(
                casServerLoginUrl, casServerUrlPrefix);
        CasClient client = new CasClient(configuration);
        client.setCallbackUrl(casService);
        client.setName("myApp1");
        Config config = new Config(new Clients(client));
        return config;
    }

    //CallbackFilter用来替代shiro-cas中的CasFilter
    @Bean
    public CallbackFilter callbackFilter() {
        CallbackFilter callbackFilter = new CallbackFilter();
        callbackFilter.setConfig(config());
        return callbackFilter;
    }
}
