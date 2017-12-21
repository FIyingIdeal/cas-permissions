package com.hiynn.caspermissions.server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author yanchao
 * @date 2017/12/19 11:55
 * @function Spring 提供的用于发送REST请求的实例配置。通过调用RestTemplate实例对应的方法发送请求和接收响应
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
