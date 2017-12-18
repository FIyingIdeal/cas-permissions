package com.hiynn.caspermissions.core.service;

import com.hiynn.caspermissions.core.remote.IRemoteServerService;
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
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanchao
 * @date 2017/12/12 17:20
 */
@Service
public class DynamicPermissionService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicPermissionService.class);

    @Resource(name = "remoteServerService")
    private IRemoteServerService remoteServerService;
    @Autowired
    private AbstractShiroFilter shiroFilter;

    @Value("${server.service.appKey}")
    private String appKey;
    @Value("${server.service.remoteServerServiceUrl}")
    private String remoteServerServiceUrl;
    @Value("${shiro.filterChainDefinitions}")
    private String filterChainDefinitions;
    @Value("${shiro.loadDBPermissionsEnabled}")
    private boolean loadDBPermissionsEnabled;
    @Value("${shiro.ignoreDBPermissionsEnabled}")
    private boolean ignoreDBPermissionsEnabled;

    @PostConstruct
    public synchronized void init() {
        logger.info("系统【{}】初始化权限开始...", appKey);
        reloadPermission();
        logger.info("系统【{}】初始化权限结束...", appKey);
    }

    public synchronized void reloadPermission() {
        Map<String, String> filterChainDefinitionMap = getFilterChainDefinitionMap();
        logger.debug("已加载权限内容：" + filterChainDefinitionMap);
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
        boolean hasFilterChainDefinitions = StringUtils.hasText(filterChainDefinitions);
        boolean hasPermissions = loadDBPermissionsEnabled || hasFilterChainDefinitions;
        if (!hasPermissions) {
            logger.warn("权限加载设置未被允许，该系统将不被进行权限保护！请查看相关设置：shiro.loadDBPermissionsEnabled" +
                    "（从数据库加载权限，默认false） 与 shiro.filterChainDefinitions（权限定义，默认为空）");
            return Collections.EMPTY_MAP;
        }
        if (loadDBPermissionsEnabled) {
            filterChainDefinitionMap.putAll(getDBFilterChainDefinitionMap());
        }
        if (hasFilterChainDefinitions) {
            filterChainDefinitionMap.putAll(getLocalDefinitionsMap());
        }
        return filterChainDefinitionMap;
    }

    protected Map<String, String> getDBFilterChainDefinitionMap() {
        logger.info("开始加载数据库动态权限...");
        Map<String, String> dbFilterChainDefinitionMap;
        try {
            dbFilterChainDefinitionMap = remoteServerService.getAppFilterChainDefinitionMap(appKey);
        } catch (BeanCreationException | RemoteConnectFailureException e) {
            dbFilterChainDefinitionMap = Collections.EMPTY_MAP;
            if (ignoreDBPermissionsEnabled) {
                logger.warn("数据库动态权限加载异常，请检查权限系统Server端状态或远程接口配置【server.service.remoteServerServiceUrl={}】" +
                        "是否正确。系统将尝试以忽略动态权限加载【shiro.ignoreDBPermissionsEnabled=true】方式启动，" +
                        "请了解该方式启动风险！！！", remoteServerServiceUrl);
            } else {
                logger.warn("数据库动态权限加载异常且动态权限加载不允许被忽略【shiro.ignoreDBPermissionsEnabled=false】，" +
                        "请检查权限系统Server端状态或远程接口配置【server.service.remoteServerServiceUrl={}】是否正确。", remoteServerServiceUrl);
                throw e;
            }
        }
        logger.debug("数据库动态权限内容：{}", dbFilterChainDefinitionMap);
        return dbFilterChainDefinitionMap;
    }

    protected Map<String, String> getLocalDefinitionsMap() {
        logger.info("开始加载配置文件自定义权限...");
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        logger.debug("配置文件自定义权限内容：{}", section);
        return section;
    }
}
