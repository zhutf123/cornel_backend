/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.util;

import lombok.Getter;

/**
 * Create By zhutf 19-11-9 下午7:06
 */
public class GenRandomCodeUtil {

    private static int DEFAULT_LENGTH = 6;

    /***
     *
     * @param length
     * @return
     */
    public static Integer genRandomCode(Integer length) {
        if (length == null) {
            length = DEFAULT_LENGTH;
        }
        return (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
    }
}
