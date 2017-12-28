package com.hiynn.caspermissions.server.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author yanchao
 * @date 2017/12/27 14:12
 * 动态权限刷新Pointcut定义。
 * 使用AOP代码结构更加清晰，且解决在启用事务的时候，会因为声明式事务未完成，在子系统中还无法获取到未提交事务的权限更新问题
 */
public class UpdateDynamicPermissionPointcut {

    /**
     * 定义在insertResource()上的Pointcut，使用private表示该Pointcut只能在本切面类中使用
     */
    @Pointcut("execution(public int com.hiynn.caspermissions.server.service.ResourceService.insertResources(java.util.List))")
    public void insertResources() {}

    @Pointcut("execution(public int com.hiynn.caspermissions.server.service.ResourceService.updateResource(com.hiynn.caspermissions.server.entity.Resource))")
    public void updateResource() {}

    @Pointcut("execution(public int com.hiynn.caspermissions.server.service.ResourceService.deleteResource(Long))")
    public void deleteResource() {}

    @Pointcut("insertResources() || updateResource() || deleteResource()")
    public void resourcePointcut() {}
}
