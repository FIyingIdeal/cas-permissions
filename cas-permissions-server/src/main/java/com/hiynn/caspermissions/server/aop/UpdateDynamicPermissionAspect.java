package com.hiynn.caspermissions.server.aop;

import com.hiynn.caspermissions.core.entity.HiynnResponseEntity;
import com.hiynn.caspermissions.core.util.CasPermissionUtils;
import com.hiynn.caspermissions.core.util.JwtUtils;
import com.hiynn.caspermissions.server.dao.ResourceMapper;
import com.hiynn.caspermissions.server.entity.Application;
import com.hiynn.caspermissions.server.entity.Resource;
import com.hiynn.caspermissions.server.service.ApplicationService;
import org.apache.shiro.authz.UnauthenticatedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author yanchao
 * @date 2017/12/27 10:43
 */
@Aspect
@Component
public class UpdateDynamicPermissionAspect implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDynamicPermissionAspect.class);
    public static final String UPDATE_DYNAMIC_PERMISSIONS_URI = "/client/updateDynamicPermission";

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${hiynn.permission.aspect.order:100}")
    private int order;

    @Around("UpdateDynamicPermissionPointcut.resourcePointcut()")
    public int resourceAspect(ProceedingJoinPoint pjp) throws Throwable {
        //先检查用户是否登录
        checkAuthentication();
        Object param = pjp.getArgs()[0];
        //判断是否是删除操作
        Resource resource = getResourceIfNecessary(param, pjp.getSignature().getName());
        int updateCount = Integer.parseInt(pjp.proceed().toString());
        invokeUpdateIfNecessary(updateCount, param, resource);
        return updateCount;
    }

    /**
     * 检查用户是否登录，如果没有登录的话抛出异常，阻止权限入库和通知子系统刷新权限操作
     */
    private void checkAuthentication() {
        if (CasPermissionUtils.getUsername() == null) {
            throw new UnauthenticatedException("用户未登录!!");
        }
    }

    /**
     * 如果是删除操作的话，需要先获取到Resource对应的AppId，以便后续通知该系统进行权限更新
     * @param param
     * @return
     */
    private Resource getResourceIfNecessary(Object param, String methodName) {
        return param instanceof Long && methodName.startsWith("delete") ?
                resourceMapper.getResourceById(Long.parseLong(param.toString())) : null;
    }

    private void invokeUpdateIfNecessary(int updateCount, Object param, Resource resource) {
        //权限未发生变化
        if (updateCount <= 0) {
            return ;
        } else if (param instanceof Resource) {
            invokeUpdate(((Resource) param).getAppId());
        } else if (param instanceof List) {
            List<Resource> resources = (List<Resource>) param;
            Long[] appIds = resources.stream().map(Resource::getAppId).distinct().toArray(Long[]::new);
            invokeUpdate(appIds);
        } else if (param instanceof Long && resource != null) {
            invokeUpdate(resource.getAppId());
        }
    }

    private void invokeUpdate(Long... appIds) {
        Stream.of(appIds).forEach(appId -> updateApplicationDynamicPermissions(appId));
    }

    /**
     * 指定该Aspect的order值，默认为100，可以通过在配置文件中设置hiynn.permission.aspect.order来修改该默认值
     * 当使用@Transaction进行事务处理的时候，其本质也是使用的AOP，此时会涉及到事务切面和动态权限更新切面的执行顺序问题：
     *     事务切面的默认Order为Ordered.LOWEST_PRECEDENCE，即优先级最低，最后执行。但实际测试的时候，如果动态权限切面的Order值没有指定的话，
     *     会先执行事务切面（此时执行顺序是不可控的），那在动态权限切面中执行权限刷新时会因为事务还未提交，而导致无法获取到最新的权限问题。
     *     所以此切面要通过指定Order来保证其执行先于事务切面。
     *     （AOP切面的执行类似于穿洋葱模型，后执行的先执行完成，先执行的后执行完成）
     * @return
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * 动态权限刷新
     * @param appId
     */
    private void updateApplicationDynamicPermissions(Long appId) {
        Application application = applicationService.getApplicationById(appId);
        String url = generateUpdateDynamicPermissionUrl(application);
        logger.debug("更新系统权限地址：{}", url);
        HttpEntity<String> httpEntity = getHttpEntity(application);
        ResponseEntity<HiynnResponseEntity> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, httpEntity, HiynnResponseEntity.class);

            if (responseEntity.getBody().getHttpStatusCode() != HttpStatus.OK.value()) {
                throw new UnauthenticatedException("未授权用户禁止刷新系统权限");
            }
            logger.info("权限更新结果：{} {}", url, responseEntity.getBody().getMessage());
        } catch (HttpClientErrorException e) {
            logger.warn("系统权限刷新地址【{}】错误，请更正后重试！", url);
            throw e;
        }

    }

    private String generateUpdateDynamicPermissionUrl(Application application) {
        String appName = application.getAppName();
        String schema = application.getAppSchema();
        String domain = application.getAppDomain();
        int port = application.getAppPort();
        String contextPath = application.getAppContextPath();
        if (!StringUtils.hasText(schema)) {
            schema = "http";
            logger.info("无法获取系统【{}】的Schema值，设置默认为{}", appName, schema);
        }
        if (!StringUtils.hasText(domain) || !StringUtils.hasText(contextPath)) {
            logger.warn("无法获取系统【{}】Domain与ContextPath值，请检查配置是否正确！", appName);
            //TODO 自定义异常
            throw new NullPointerException("请求Domain（IP或域名）与ContextPath不允许为空【无ContextPath请指定为'/'】");
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(schema).append("://")
                .append(domain).append(":")
                .append(port).append(contextPath)
                .append(UPDATE_DYNAMIC_PERMISSIONS_URI);
        return builder.toString();
    }

    private HttpEntity<String> getHttpEntity(Application application) {
        String token = generateRequestToken(application, CasPermissionUtils.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authentication", token);
        return new HttpEntity<>(null, headers);
    }

    private String generateRequestToken(Application application, String username) {
        Map<String, Object> message = new HashMap<>();
        message.put("appId", application.getId());
        message.put("username", username);
        String securityKey = application.getAppKey();
        return JwtUtils.createJWT("server", "clients", TimeUnit.MINUTES.toMillis(1), message, securityKey);
    }
}
