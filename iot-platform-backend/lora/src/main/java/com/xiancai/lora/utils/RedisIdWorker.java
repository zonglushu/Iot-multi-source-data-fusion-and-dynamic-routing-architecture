package com.xiancai.lora.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {

    /**
     * 开始的时间戳（2022年1月1日0时0分0秒）
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static final long BEGIN_TIMESTAMP=1640995200;

    private static final int COUNT_BITS=32;
    //不同的业务，用不同的自增策略
    public long nextId(String keyPrefix){
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 2.生成序列号,
        // 2.1 获取当前日期，精确到天，如果按月的话，yyyy:MM:dd就好了，redis的key可以按冒号自动分层
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 不会出现null，因为 increment命令当key不存在时，会自动创建
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        // 3.拼接并返回
        // 时间戳向左移动32位，为序列号留出32位,然后留出来的32位都是0，然后进行或运算，
        // 与0做或运算就相当于把这个数本身保留下来了(梦回计算机导论)
        return timestamp << COUNT_BITS | count;
    }

    // 生成一个任意时间的时间戳，然后ID
    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        long second = time.toEpochSecond(ZoneOffset.UTC);
        System.out.println(second);
    }
}
