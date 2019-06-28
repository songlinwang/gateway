package com.wsl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wsl
 * @date 2019/5/21
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private RedisTemplate redisTemplate;

    @Cacheable(value = "getUserName")
    public String getUserName() {
        List<String> keys = new ArrayList<>();
        redisTemplate.opsForList().leftPush("queue", "1");
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i < 10000; i++) {
                    redisConnection.lPush("queue_2".getBytes(), String.valueOf(i).getBytes());
                }
                return null;
            }
        });
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (int i = 0; i < 10000; i++) {
                    connection.lPush("queue_2".getBytes(), String.valueOf(i).getBytes());
                }
                return null;
            }
        });
        List<String> result = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (String key : keys) {
                    redisConnection.get(key.getBytes());
                }
                return null;
            }
        });
        List<String> resultTemp = redisTemplate.opsForValue().multiGet(keys);
        return "string";
    }

    public void testTemplate() {
        exec("123", new ListenerTest() {
            @Override
            public void onsuccess() {
                log.error("success");
            }

            @Override
            public void onfail() {
                log.error("fail");
            }
        });
    }

    public void exec(String exec, ListenerTest listenerTest) {
        if (exec.equals("123")) {
            listenerTest.onsuccess();
        } else {
            listenerTest.onfail();
        }

    }

    public interface ListenerTest {
        void onsuccess();

        void onfail();
    }
}
