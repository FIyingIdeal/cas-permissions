package com.hiynn.caspermissions.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author yanchao
 * @date 2017/12/1 11:58
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CORSInterceptor()).addPathPatterns("/**");
        //super.addInterceptors(registry);
    }
}