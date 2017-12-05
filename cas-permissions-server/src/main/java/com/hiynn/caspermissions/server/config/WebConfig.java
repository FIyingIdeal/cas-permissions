package com.hiynn.caspermissions.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanchao
 * @date 2017/12/1 11:58
 * 使用Interceptor解决跨域问题，该方式是在方法级别进行控制
 */
//@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CORSInterceptor()).addPathPatterns("/**");
        //super.addInterceptors(registry);
    }

    static class CORSInterceptor extends HandlerInterceptorAdapter {
        private String origin  = "";
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            origin = request.getHeader("Origin");
            System.out.println("InterceptorAdapter " + origin);
            //设置允许携带身份信息，即cookie
            response.addHeader("Access-Control-Allow-Credentials", "true");
            //指定服务器端允许进行跨域资源访问的来源域，使用通配符*表示允许任何域的JavaScript资源访问
            //但是如果将Access-Control-Allow-Credentials设置为ture的话，Access-Control-Allow-Origin必须是一个明确的域，而不能使用通配符
            response.addHeader("Access-Control-Allow-Origin", origin);
            //指定服务器允许进行跨域资源访问的请求方法列表，一般用在响应预检请求上
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTION");
            //指定服务器允许进行跨域资源访问的请求头列表，一般用在响应预检请求上
            response.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Accept");

            return true;
        }
    }
}

