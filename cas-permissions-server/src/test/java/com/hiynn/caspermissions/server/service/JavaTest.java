package com.hiynn.caspermissions.server.service;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanchao
 * @date 2017/12/12 13:57
 */
public class JavaTest {

    @Test
    public void splitTest() {
        String str = "1";
        String[] items = str.split(",");
        System.out.println(items.length);
    }

    @Test
    public void streamMapTest() {
        String str = "1,2,3,";
        List<String> strs = Arrays.asList(str.split(","));
        Set<Long> nums = strs.stream().map(Long::parseLong).collect(Collectors.toSet());
        System.out.println(nums);
    }

    @Test
    public void mapTest() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("1", "1");
        map.putAll(map1);
        Map<String, String> map2 = new HashMap<>();
        map2.put("2", "2");
        map.putAll(map2);
        System.out.println(map);
    }
}
