package com.wsl.redis;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 模拟guava里面的bitsArray
 *
 * @author wsl
 * @date 2019/6/11
 */
public class RedisMaps {

    @Resource
    private RedisTemplate redisTemplate;

    private static final String BASE_KEY = "bloomfilter";

    private long bitSize;

    RedisMaps(long bitSize) {
        this.bitSize = bitSize;
    }

    boolean get(Long[] offsets) {
        for (int i = 0; i < offsets.length; i++) {
            if (redisTemplate.opsForValue().getBit(BASE_KEY, offsets[i])) {
                return false;
            }
        }
        return true;
    }

    boolean set(Long[] offsets) {
        for (int i = 0; i < offsets.length; i++) {
            boolean result = redisTemplate.opsForValue().setBit(BASE_KEY, offsets[i], true);

        }
        return true;
    }

    long bitSize() {
        return this.bitSize;
    }

}
