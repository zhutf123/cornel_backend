/**
 */
package com.demai.cornel.util;

import java.util.Random;

/**
 * Create By zhutf 19-11-14 下午10:20
 */
public class GenerateCodeUtils {

    // 使用到Algerian字体，系统里没有的话需要安装字体，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
    public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    public static final String VERIFY_NUMBER_CODES = "0123456789";

    /**
     * 使用系统默认字符源生成验证码
     * 
     * @param randomSize 验证码长度
     * @return
     */
    public static String generateRandomCode(int randomSize) {
        return generateRandomCode(randomSize, VERIFY_CODES);
    }

    /**
     * 使用系统默认字符源生成纯数字验证码
     *
     * @param randomSize 验证码长度
     * @return
     */
    public static String generateNumRandomCode(int randomSize) {
        return generateRandomCode(randomSize, VERIFY_NUMBER_CODES);
    }
    /**
     * 使用指定源生成验证码
     * 
     * @param randomSize 验证码长度
     * @param sources 验证码字符源
     * @return
     */
    public static String generateRandomCode(int randomSize, String sources) {
        if (sources == null || sources.length() == 0) {
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder randomCode = new StringBuilder(randomSize);
        for (int i = 0; i < randomSize; i++) {
            randomCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return randomCode.toString();
    }


}