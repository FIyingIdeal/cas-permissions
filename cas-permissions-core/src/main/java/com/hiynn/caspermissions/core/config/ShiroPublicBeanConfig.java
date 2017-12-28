package com.hiynn.caspermissions.core.config;

import com.hiynn.caspermissions.core.shiro.HiynnCasPermissionPac4jRealm;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanchao
 * @date 2017/12/13 17:28
 */
@Configuration
public class ShiroPublicBeanConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroPublicBeanConfig.class);

    @Bean
    public Realm pac4jRealm() {
        logger.info("realm配置开始...");
        HiynnCasPermissionPac4jRealm realm = new HiynnCasPermissionPac4jRealm();
        return realm;
    }

    @Bean(name = "subjectFactory")
    public Pac4jSubjectFactory subjectFactory() {
        return new Pac4jSubjectFactory();
    }
}
