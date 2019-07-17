package com.plasticlove.test;

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


    @Test
    public void testSetNx(){
        System.out.println(redisClient.setNx("nx","1111",4000L));
    }
    @Test
    public void testGet(){
        System.out.println((String) redisClient.get("maoyan"));
    }
}
