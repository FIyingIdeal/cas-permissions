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

    @Value("${casServerUrl}")
    private String casServerUrl;

    @Value("${shiro.cas.casServerLoginUrl}")
    private String casServerLoginUrl;

    @Value("${shiro.cas.callbackUrl}")
    private String callbackUrl;

    @Bean
    public Config config() {
        CasConfiguration configuration = new CasConfiguration(
                casServerLoginUrl, casServerUrl);
        CasClient client = new CasClient(configuration);
        client.setCallbackUrl(callbackUrl);
        Config config = new Config(client);
        return config;
    }

    @Bean
    public CallbackFilter getCallbackFilter() {
        CallbackFilter callbackFilter = new CallbackFilter();
        callbackFilter.setConfig(config());
        return callbackFilter;
    }
}
