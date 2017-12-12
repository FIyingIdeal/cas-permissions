package com.hiynn.caspermissions.client.service;

import com.hiynn.caspermissions.core.remote.IRemoteService;
import org.apache.shiro.config.Ini;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanchao
 * @date 2017/12/12 17:20
 */
@Service
public class DynamicPermissionService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicPermissionService.class);

    @Resource(name = "remoteService")
    private IRemoteService remoteService;

    @Autowired
    private AbstractShiroFilter shiroFilter;

    @Value("${shiro.filterChainDefinitions}")
    private String filterChainDefinitions;

    @Value("${server.service.appKey}")
    private String appKey;

    @PostConstruct
    public synchronized void init() {
        logger.info("权限加载开始");
        reloadPermission();
        logger.info("权限加载结束");
    }

    public synchronized void reloadPermission() {
        Map<String, String> filterChainDefinitionMap = getFilterChainDefinitionMap();
        //TODO 测试使用debug方式，配置logback进行日志测试
        logger.info("获取到的权限：" + filterChainDefinitionMap);
        DefaultFilterChainManager manager = (DefaultFilterChainManager) getFilterChainManager();
        manager.getFilterChains().clear();
        addToChain(manager, filterChainDefinitionMap);
    }

    protected FilterChainManager getFilterChainManager() {
        PathMatchingFilterChainResolver filterChainResolver =
                (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        return filterChainResolver.getFilterChainManager();
    }

    protected void addToChain(DefaultFilterChainManager manager, Map<String, String> filterChainMap) {
        NamedFilterList allPatternFilterList = manager.getFilterChains().remove("/**");
        filterChainMap.forEach((k, v) -> {
            manager.createChain(k, v);
        });

        //添加  /**  的拦截器链
        if (!CollectionUtils.isEmpty(allPatternFilterList)) {
            manager.getFilterChains().put("/**", allPatternFilterList);
        }
    }

    protected Map<String, String> getFilterChainDefinitionMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.putAll(remoteService.getAppFilterChainDefinitionMap(appKey));
        if (StringUtils.hasText(filterChainDefinitions)) {
            filterChainDefinitionMap.putAll(getDefinitionsMap());
        }
        return filterChainDefinitionMap;
    }

    protected Map<String, String> getDefinitionsMap() {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        return section;
    }
}
