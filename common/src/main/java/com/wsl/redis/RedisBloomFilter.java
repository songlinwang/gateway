package com.wsl.redis;

import com.google.common.hash.Funnel;

/**
 * @author wsl
 * @date 2019/6/11
 */
public class RedisBloomFilter<T> {

    RedisMaps redisMaps;
    int numHashFunctions;

    RedisBloomFilter(RedisMaps redisMaps, int numHashFunctions) {

    }

    public static <T> RedisBloomFilter<T> create(Funnel<? extends T> funnel, long expectInsertions, double fpp, RedisBloomStrategy redisBloomStrategy) {

        long numBits = optimalNumOfBits(expectInsertions, fpp);
        int numHashFunctions = optimalNumOfHashFunctions(expectInsertions, numBits);
        try {
            return new RedisBloomFilter<>(new RedisMaps(numBits), numHashFunctions);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 针对于插入数据的大小  计算出最合适的数组容量
     *
     * @param n
     * @param p
     * @return
     */
    private static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 针对于插入数据的大小 ---> 计算出合适的数组数量 ---> 计算出最合适的hash函数个数
     */
    static int optimalNumOfHashFunctions(long n, long m) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}
