package com.dhu.common.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 产生随机数
 */
public class RandomUtil extends RandomStringUtils {
    /**
     * NUMBER：表示生成数字随机数
     * NUMBER_LETTER： 表示生成数字和字母混合的随机数
     */
    public static enum RANDOM_TYPE {
        NUMBER, NUMBER_LETTER
    };

    /**
     * 数字的下标范围
     */
    public static final int NUMBER_IDX_SCOPE = 10;
    /**
     * 数字和字母的下标范围
     */
    public static final int NUMBER_LETTER_IDX_SCOPE = 62;

    /**
     * 数字、字母表
     */
    public static final char [] NUMBER_LETTER_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'};

    /**
     * 生成主键， 规则： 根据系统当前时间+通过getRandomChar生成的15位随机数
     * @return String
     */
    public static String getPrimaryKey() {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(now) + getRandomNumberLetter(15).toUpperCase();
    }
    
    /**
     * 生成一个文件名，股则：根据系统当前时间+通过getRandomChar生成的8位随机数
     * @return
     */
    public static String getRandomFileName() {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(now) + getRandomNumberLetter(8).toUpperCase();
    }
    
    /**
     * 获取数字组成的随机数
     * @param count             设置随机数的位数
     * @return
     */
    public static String getRandomNumber(int count){
        return getRandomValue(count, RANDOM_TYPE.NUMBER);
    }

    /**
     * 获取数字和字母组成的随机数
     * @param count             设置随机数的位数
     * @return
     */
    public static String getRandomNumberLetter(int count){
        return getRandomValue(count, RANDOM_TYPE.NUMBER_LETTER);
    }

    /**
     * 获取随机数底层方法实现
     * @param count                    设置随机数的位数
     * @param type                      生成数字随机数或数字和字母混合的随机数
     * @return
     */
    public static String getRandomValue(int count, RANDOM_TYPE type){
        int idxScope = (RANDOM_TYPE.NUMBER.equals(type)) ? NUMBER_IDX_SCOPE : NUMBER_LETTER_IDX_SCOPE;
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < count; i++) {
            buffer.append(NUMBER_LETTER_TABLE[random.nextInt(idxScope)]);
        }

        return buffer.toString();
    }
}
