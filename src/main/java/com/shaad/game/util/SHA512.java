package com.shaad.game.util;

import java.security.MessageDigest;

public class SHA512 {
    public static String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes());
            byte byteData[] = md.digest();

            StringBuilder hashCodeBuffer = new StringBuilder();
            for (byte aByteData : byteData) {
                hashCodeBuffer.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }
            return hashCodeBuffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
