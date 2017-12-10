package com.hiynn.caspermissions.client.config;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @author yanchao
 * @date 2017/12/10 19:26
 */
@Configuration
public class RemoteServiceConfig {

    @Bean
    public HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean() {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean =
                new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceUrl("http://localhost:8080/hiynn-cas-server/remoteService");
        httpInvokerProxyFactoryBean.setServiceInterface(IRemoteService.class);
        return httpInvokerProxyFactoryBean;
    }
}
