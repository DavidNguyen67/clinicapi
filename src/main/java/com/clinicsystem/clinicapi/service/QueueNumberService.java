package com.clinicsystem.clinicapi.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueueNumberService {

    private final StringRedisTemplate redisTemplate;

    private String getTodayKey() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "queue:number:" + today;
    }

    public int getNextQueueNumber() {
        String key = getTodayKey();

        Long number = redisTemplate.opsForValue().increment(key);

        redisTemplate.expire(key, Duration.ofDays(2));

        return number.intValue();
    }

    public int getTodayCurrentNumber() {
        String value = redisTemplate.opsForValue().get(getTodayKey());
        return value == null ? 0 : Integer.parseInt(value);
    }
}