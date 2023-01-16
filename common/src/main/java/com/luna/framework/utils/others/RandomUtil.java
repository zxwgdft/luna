package com.luna.framework.utils.others;

import com.luna.framework.api.SystemException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机值产生工具
 *
 * @author TontoZhou
 */
public class RandomUtil {

    private static char[] simpleCharArray = {'_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z'};

    /**
     * 随机产生一个整数
     */
    public static int getRandomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    /**
     * 在0到{@code max}之间随机产生一个数
     */
    public static int getRandomInt(int max) {
        return getRandomInt(0, max);
    }

    /**
     * 在{@code min}和{@code max}范围内随机产生一个整数
     */
    public static int getRandomInt(int min, int max) {
        int d = max - min;
        if (d < 0) {
            throw new SystemException(SystemException.CODE_ERROR_DATA);
        }
        return ThreadLocalRandom.current().nextInt(d) + min;
    }

    /**
     * 随机产生一个之间的双精度浮点数
     */
    public static double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * 随机产生一个0到{@code max}的双精度浮点数
     */
    public static double getRandomDouble(double max) {
        return getRandomDouble(0, max);
    }

    /**
     * 随机产生一个{@code min}到{@code max}的双精度浮点数
     */
    public static double getRandomDouble(double min, double max) {
        double d = max - min;
        if (d < 0) {
            throw new SystemException(SystemException.CODE_ERROR_DATA);
        }
        return min + (max - min) * ThreadLocalRandom.current().nextDouble();
    }

    /**
     * 随机产生一个长度2到10之间的字符串
     */
//    public static String getRandomString() {
//        return getRandomString(2, 10);
//    }

    /**
     * 随机产生一个{@code minlength}到{@code maxlength}之间长度的字符串
     */
//    public static String getRandomString(int minlength, int maxlength) {
//        int d = maxlength - minlength;
//        if (d <= 0) {
//            throw new SystemException(SystemException.CODE_ERROR_DATA);
//        }
//        return getRandomString(ThreadLocalRandom.current().nextInt(d) + minlength);
//    }

    /**
     * 随机产生一个指定长度的字符串
     *
     * @param length
     * @return
     */

    // 随机字符串算法需要重写
//    public static String getRandomString(int length) {
//        if (length <= 0)
//            return "";
//        Random random = ThreadLocalRandom.current();
//        int edge = simpleCharArray.length - 1;
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            sb.append(simpleCharArray[random.nextInt(edge)]);
//        }
//        return sb.toString();
//    }

    /**
     * 随机产生一个长整数
     *
     * @return
     */
    public static long getRandomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    /**
     * 随机产生一个范围在0到{@code max}之间的长整数
     */
    public static long getRandomLong(long max) {
        return getRandomLong(0, max);
    }

    /**
     * 随机产生一个范围在{@code min}到{@code max}之间的长整数
     */
    public static long getRandomLong(long min, long max) {
        return (long) getRandomDouble((double) min, (double) max);
    }

}
