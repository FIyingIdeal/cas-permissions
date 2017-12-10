package com.hiynn.caspermissions.server.config;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import com.hiynn.caspermissions.server.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * @author yanchao
 * @date 2017/12/10 19:03
 */
@Configuration
public class RemoteServiceConfig {

    @Autowired
    private RemoteService remoteService;

    @Bean("/remoteService")
    public HttpInvokerServiceExporter httpInvokerServiceExporter() {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(remoteService);
        exporter.setServiceInterface(IRemoteService.class);
        return exporter;
    }
}
