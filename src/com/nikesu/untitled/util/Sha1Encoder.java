package com.nikesu.untitled.util;

import com.mysql.cj.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Encoder {
    /**
     * 使用 SHA-1 加密字符串
     * @param str
     * @return
     */
    public static String getSha1(String str) {
        if (StringUtils.isNullOrEmpty(str)) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes());
            return toHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            // 11110000
            sb.append(Character.forDigit((bytes[i] & 240) >> 4, 16));
            // 00001111
            sb.append(Character.forDigit(bytes[i] & 15, 16));
        }

        return sb.toString();
    }
}
