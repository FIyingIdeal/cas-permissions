package com.hiynn.caspermissions.server.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author yanchao
 * @date 2017/12/19 11:58
 */
@RestController
public class RestTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/rest")
    public String getRequestResult() {
        String result = restTemplate.getForObject("http://192.168.3.20:8888/castest/client/updateDynamicPermission", String.class);
        logger.info("接收到的请求响应内容：{}", result);
        return result;
    }
}
