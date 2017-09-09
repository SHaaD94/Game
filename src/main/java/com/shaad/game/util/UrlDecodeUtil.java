package com.shaad.game.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URLDecoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlDecodeUtil {
    public static String decode(String string) {
        try {
            return URLDecoder.decode(string, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
