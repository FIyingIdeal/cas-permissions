package com.hiynn.caspermissions.client.config;

import com.hiynn.caspermissions.core.config.AbstractShiroConfig;
import io.buji.pac4j.realm.Pac4jRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
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
public class ShiroConfig extends AbstractShiroConfig {

    @Autowired
    protected SecurityManager securityManager;

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

    /**
     * shiro-spring-boot-web-starter中默认会注入一个简单的ShiroFilterFactoryBean实例
     * {@link ShiroWebFilterConfiguration#shiroFilterFactoryBean()}
     * TODO 但这个默认的实例在构造拦截器链的时候只能使用Shiro自定义的filter，这里提供了一个FilterRegistrationBean用来注册Filter，
     *      但不知道如何指定Filter Name，这在构造拦截器链的时候是没法使用自定义的Filter的。
     * TODO http://www.hillfly.com/2017/179.html 在此已提问，期待回复
     *
     * 这里自定义了一个ShiroFilterFactoryBean，并注册为Bean提供服务
     * @return
     */
    @Bean
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();

        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setLoginUrl(loginUrl);
        //filterFactoryBean.setSuccessUrl(successUrl);
        filterFactoryBean.setFilters(registerUserFilter());

        /**
         * 注册FilterChainDefinitionMap，shiro 4.0提供了{@link DefaultShiroFilterChainDefinition}用来辅助设置该属性
         * TODO 但需要注意的是该辅助类中使用的是HashMap，可能会引发FilterChain的顺序问题。
         *
         * 出于灵活性考虑，可以将拦截器链的配置在配置文件中以字符串的形式设置，每一个拦截器链的设置以\n进行分隔，
         * 然后调用{@link ShiroFilterFactoryBean#setFilterChainDefinitions(String)}方法，Shiro会自动将其进行解析。
         *
         * TODO 如果要做动态权限管理，那么拦截器链的定义应该是从数据库获取并动态拼接。
         *
         */
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return filterFactoryBean;
    }

}
