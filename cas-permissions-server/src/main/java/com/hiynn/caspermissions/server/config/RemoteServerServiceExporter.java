package com.hiynn.caspermissions.server.config;

import com.hiynn.caspermissions.core.remote.IRemoteServerService;
import com.hiynn.caspermissions.server.service.RemoteServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * @author yanchao
 * @date 2017/12/10 19:03
 * @function 本地Server接口暴露，当Client接收到以后就可以直接使用这个Bean
 */
@Configuration
public class RemoteServerServiceExporter {

    @Autowired
    private RemoteServerService remoteServerService;

    @Bean("/remoteServerService")
    public HttpInvokerServiceExporter httpInvokerServiceExporter() {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(remoteServerService);
        exporter.setServiceInterface(IRemoteServerService.class);
        return exporter;
    }
}
