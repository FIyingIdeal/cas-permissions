package com.hiynn.caspermissions.core;

import org.apache.shiro.codec.Hex;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author yanchao
 * @date 2017/12/14 16:44
 */
public class CustomPasswordEncoder {

    @Test
    public void test() {
        System.out.println("123");
    }

    @Test
    public void passwordEncod() {
        String password = "123456";//fd32e054edde3dfa430f2384370b5e7e
        String encodedPassword = "fd32e054edde3dfa430f2384370b5e7e";
        String salt = "a522078f8b8bcccc341ba1268a8c99a6";
        String encodingAlgorithm = "MD5";
        String characterEncoding = "UTF-8";
        int hashIterations = 2;
        try {
            byte[] pswBytes = password.getBytes(characterEncoding);
            byte[] saltBytes = salt.getBytes(characterEncoding);
            MessageDigest digest = MessageDigest.getInstance(encodingAlgorithm);
            if (saltBytes != null) {
                digest.reset();
                digest.update(saltBytes);
            }
            byte[] hashed = digest.digest(pswBytes);
            int iterations = hashIterations - 1;
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                hashed = digest.digest(hashed);
            }
            System.out.println(Hex.encodeToString(hashed).equals(encodedPassword));
            System.out.println(Arrays.toString(org.apache.commons.codec.binary.Hex.decodeHex(encodedPassword)).equals(Arrays.toString(hashed)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
