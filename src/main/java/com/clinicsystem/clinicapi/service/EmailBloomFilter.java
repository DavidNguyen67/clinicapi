package com.clinicsystem.clinicapi.service;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailBloomFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BLOOM_FILTER_KEY = "bloom:emails";
    private static final int NUM_HASH_FUNCTIONS = 7;
    private static final int BIT_SIZE = 1_000_000;

    public void add(String email) {
        long[] indexes = getHashIndexes(email);
        for (long index : indexes) {
            redisTemplate.opsForValue().setBit(BLOOM_FILTER_KEY, index, true);
        }
    }

    public boolean mightExist(String email) {
        long[] indexes = getHashIndexes(email);
        for (long index : indexes) {
            Boolean bit = redisTemplate.opsForValue().getBit(BLOOM_FILTER_KEY, index);
            if (bit == null || !bit)
                return false;
        }
        log.info("Bloom filter indicates email {} might exist", email);
        return true;
    }

    private long[] getHashIndexes(String email) {
        long[] indexes = new long[NUM_HASH_FUNCTIONS];
        byte[] bytes = email.getBytes(StandardCharsets.UTF_8);
        long hash1 = Hashing.murmur3_128().hashBytes(bytes).asLong();
        long hash2 = Hashing.sha256().hashBytes(bytes).asLong();
        for (int i = 0; i < NUM_HASH_FUNCTIONS; i++) {
            indexes[i] = Math.abs((hash1 + i * hash2) % BIT_SIZE);
        }
        return indexes;
    }
}