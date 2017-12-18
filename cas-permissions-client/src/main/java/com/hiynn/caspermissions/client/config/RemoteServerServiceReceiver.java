package com.hiynn.caspermissions.client.config;

import com.hiynn.caspermissions.core.remote.IRemoteServerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @author yanchao
 * @date 2017/12/10 19:26
 * @function 远程Server暴露接口接收
 */
@Configuration
public class RemoteServerServiceReceiver {

    @Value("${server.service.remoteServerServiceUrl}")
    private String remoteServerServiceUrl;

    @Bean(name = "remoteServerService")
    public HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean() {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean =
                new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceUrl(remoteServerServiceUrl);
        httpInvokerProxyFactoryBean.setServiceInterface(IRemoteServerService.class);
        return httpInvokerProxyFactoryBean;
    }
}
