package com.plasticlove.redis;

import com.plasticlove.redis.client.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class RedisClientTest {
    @Autowired
    private RedisClient redisClient;
    @Test
    public void testConnection(){
        System.out.println(redisClient.isConnected());
    }

    @Test
    public void testSet(){
        redisClient.set("maoyan","hello");
    }
}
