package com.hiynn.caspermissions.client.config;

import com.hiynn.caspermissions.core.config.AbstractShiroConfig;
import io.buji.pac4j.realm.Pac4jRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
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
public class ShiroConfig extends AbstractShiroConfig {

    /**
     * 使用buji-pac4j提供的Pac4jRealm进行身份认证
     * TODO 权限需要定制化的话要继承Pac4jRealm并重写doGetAuthorizationInfo(PrincipalCollection)方法
     * @return
     */
    @Bean
    public Realm pac4jRealm() {
        Pac4jRealm realm = new Pac4jRealm();
        return realm;
    }

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
     * 定义拦截器链
     * @return
     */
    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition filterChainDefinition = new DefaultShiroFilterChainDefinition();
        filterChainDefinition.addPathDefinition("/logout", "logout");
        filterChainDefinition.addPathDefinition("/service/**", "cors, securityFilter");
        filterChainDefinition.addPathDefinition("/callback", "cas");
        filterChainDefinition.addPathDefinition("/**", "anon");
        return filterChainDefinition;
    }

}
