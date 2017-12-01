package com.hiynn.caspermissions.server.config;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/5/10.
 * 使用Interceptor解决跨域资源访问
 */
public class CORSInterceptor extends HandlerInterceptorAdapter {

    //private String origin  = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //origin = request.getHeader("Origin");
        //System.out.println("InterceptorAdapter " + origin);
        //设置允许携带身份信息，即cookie
        response.addHeader("Access-Control-Allow-Credentials", "true");
        //指定服务器端允许进行跨域资源访问的来源域，使用通配符*表示允许任何域的JavaScript资源访问
        //但是如果将Access-Control-Allow-Credentials设置为ture的话，Access-Control-Allow-Origin必须是一个明确的域，而不能使用通配符
        response.addHeader("Access-Control-Allow-Origin", "*");
        //指定服务器允许进行跨域资源访问的请求方法列表，一般用在响应预检请求上
        response.addHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTION");
        //指定服务器允许进行跨域资源访问的请求头列表，一般用在响应预检请求上
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Accept");

        return true;
    }
}
