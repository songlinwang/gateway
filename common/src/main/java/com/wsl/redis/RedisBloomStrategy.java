package com.wsl.redis;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * @author wsl
 * @date 2019/6/11
 */
public class RedisBloomStrategy {

    public boolean put(String string, int numHashFunctions, RedisMaps bits) {
        long bitSize = bits.bitSize();
        byte[] bytes = Hashing.murmur3_128().hashString(string, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);

        boolean bitsChanged = false;
        long combinedHash = hash1;
//        for (int i = 0; i < numHashFunctions; i++) {
//            bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
//            combinedHash += hash2;
//        }
        Long[] offsets = new Long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            offsets[i] = (combinedHash & Long.MAX_VALUE) % bitSize;
            combinedHash += hash2;
        }
        bitsChanged = bits.set(offsets);
        // bits.ensureCapacityInternal();//自动扩容
        return bitsChanged;
    }


    public boolean mightContain(String object, int numHashFunctions, RedisMaps bits) {
        long bitSize = bits.bitSize();
        byte[] bytes = Hashing.murmur3_128().hashString(object, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);
        long combinedHash = hash1;
//        for (int i = 0; i < numHashFunctions; i++) {
//            if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
//                return false;
//            }
//            combinedHash += hash2;
//        }
//        return true;
        Long[] offsets = new Long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            offsets[i] = (combinedHash & Long.MAX_VALUE) % bitSize;
            combinedHash += hash2;
        }
        return bits.get(offsets);
    }

    //todo
    private long lowerEight(byte[] bytes) {
        return 0;
    }

    //todo
    private long upperEight(byte[] bytes) {
        return 0;
    }
}
