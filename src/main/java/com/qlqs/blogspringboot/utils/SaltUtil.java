package com.qlqs.blogspringboot.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.Random;

public class SaltUtil {
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890!@#$%^&*()".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String salt = getSalt(10);
        Md5Hash md5Hash = new Md5Hash("biao456789",salt,1024);
        System.out.println(salt+":"+md5Hash.toHex());
    }
}
