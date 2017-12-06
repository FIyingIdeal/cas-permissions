package com.hiynn.caspermissions.core.config;

import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.pac4j.core.config.Config;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanchao
 * @date 2017/12/6 18:21
 */
public abstract class AbstractShiroConfig {

    @Value("${shiro.loginUrl}")
    protected String loginUrl;

    @Value("${shiro.successUrl}")
    protected String successUrl;

    @Value("${shiro.cas.callbackFilterDefaultUrl}")
    protected String callbackFilterDefaultUrl;

    @Value("${shiro.cas.logoutFilterDefaultUrl}")
    protected String logoutFilterDefaultUrl;

    @Autowired
    protected SecurityManager securityManager;

    @Autowired
    @Qualifier(value = "loginPageConfig")
    protected Config config;

    @Bean(name = "subjectFactory")
    public Pac4jSubjectFactory subjectFactory() {
        return new Pac4jSubjectFactory();
    }

    /**
     * 此处不要将Filter注册为Bean，通过{@link ShiroFilterFactoryBean#setFilters(Map)}来向Shiro中注入Filter
     * 如果使用@Bean的方式注册为了一个Bean，且同时被注册到了Shiro当中，那这个Bean会被Spring和Shiro两者管理，在shiro的Filter执行完毕以后
     * 这里的Filter还会在执行{@link org.apache.shiro.web.servlet.ProxiedFilterChain#doFilter(ServletRequest, ServletResponse)}
     * 中的{@code this.orig.doFilter(...)}（即执行非Shiro的Filter，当与Spring整合的时候会执行Spring的Filter）时执行
     * 那同一个Filter就会执行多次，这并不是我们所期望的，且可能会出错...                困扰了整整一天...
     * @return
     */
    protected SecurityFilter securityFilter() {
        SecurityFilter casSecurityFilter = new SecurityFilter();
        casSecurityFilter.setConfig(config);
        casSecurityFilter.setClients("CasClient");
        //casSecurityFilter.setClients("CasRestFormClient");
        /*
        //设置了rest方式的将身份信息保存到session当中，但无法解决第二个系统不需要在请求中携带username和password即可登录的问题
        RestSecurityLogin restSecurityLogin = new RestSecurityLogin();
        restSecurityLogin.setSaveProfileInSession(true);
        restSecurityLogin.setProfileManagerFactory(ShiroProfileManager::new);
        casSecurityFilter.setSecurityLogic(restSecurityLogin);*/
        return casSecurityFilter;
    }

    /**
     * CallbackFilter用来替代shiro-cas中的CasFilter，该Filter做的事：
     * 验证ticket是否有效
     * TODO 该Filter其他任务有待研究
     */
    protected CallbackFilter callbackFilter() {
        CallbackFilter callbackFilter = new CallbackFilter();
        callbackFilter.setConfig(config);
        callbackFilter.setMultiProfile(true);
        /**
         * 设置默认的callback跳转页面
         * 默认情况下，当访问受保护的页面的时候，如果没有登录，会先保存请求路径到session中，然后跳转到登录页面，登录成功后会跳转回原始请求页面
         * 如果从session中没有拿到对应的请求页面，默认会跳转到"/"，可以通过setDefaultUrl(String)更改默认跳转
         */
        callbackFilter.setDefaultUrl(callbackFilterDefaultUrl);
        return callbackFilter;
    }

    /**
     * 控制跨域的Filter
     * @return
     */
    protected CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * 退出Filter
     * @return
     */
    protected LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setConfig(config);
        logoutFilter.setLocalLogout(true);
        logoutFilter.setCentralLogout(true);
        logoutFilter.setDefaultUrl(logoutFilterDefaultUrl);
        return logoutFilter;
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

    /**
     * 注册自定义的Filter
     * @return
     */
    protected Map<String, Filter> registerUserFilter() {
        Map<String, Filter> shiroFilter = new LinkedHashMap<>();
        //SecurityFilter是pac4j-shiro提供的Filter，每一个受保护（需要登录权限）的URL都要通过该Filter进行验证
        shiroFilter.put("securityFilter", securityFilter());
        //CallbackFilter是pac4j-shiro提供的Filter，控制身份验证后调回原请求
        shiroFilter.put("cas", callbackFilter());
        shiroFilter.put("cors", corsFilter());
        shiroFilter.put("logout", logoutFilter());
        return shiroFilter;
    };

    /**
     * 定义拦截器链，由子类实现
     * @return
     */
    protected abstract ShiroFilterChainDefinition shiroFilterChainDefinition();
}
