package com.hiynn.caspermissions.client.config;

import com.hiynn.caspermissions.core.config.AbstractShiroFilterConfig;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Map;

/**
 * @author yanchao
 * @date 2017-12-6 21:04:46
 * @see ShiroWebFilterConfiguration
 */
@Configuration
public class ShiroFilterConfig extends AbstractShiroFilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroFilterConfig.class);

    @Autowired
    protected SecurityManager securityManager;

    /**
     * 注册自定义的Filter
     * @return
     */
    @Override
    protected Map<String, Filter> registerUserFilter() {
        Map<String, Filter> shiroFilter = super.registerUserFilter();
        return shiroFilter;
    }

    /**
     * shiro-spring-boot-web-starter中默认会注入一个简单的ShiroFilterFactoryBean实例
     * {@link ShiroWebFilterConfiguration#shiroFilterFactoryBean()}
     * 这里自定义了一个ShiroFilterFactoryBean，并注册为Bean提供服务
     * @return
     */
    @Bean
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        logger.info("ShiroFilterFactoryBean配置开始...");
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setLoginUrl(loginUrl);
        filterFactoryBean.setFilters(registerUserFilter());
        //拦截器链的配置由动态权限部分控制
        return filterFactoryBean;
    }

}
