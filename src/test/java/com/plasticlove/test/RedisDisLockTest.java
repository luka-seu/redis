package com.plasticlove.test;

import com.google.common.collect.Lists;
import com.plasticlove.redis.dislock.RedisDisLock;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class RedisDisLockTest {
    private static final String LOCK = "lock";

    @Autowired
    private RedisDisLock redisDisLock;

    ArrayList<Integer> list = Lists.newArrayList();
    @Test
    public void testDisLock(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName());
                    if (redisDisLock.lock(LOCK,50000L)) {
                        list.add(1);
                        redisDisLock.unlock(LOCK);
                        break;
                    }
                }
            }
        });
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName());
                    if (redisDisLock.lock(LOCK,50000L)) {
                        list.add(2);
                        redisDisLock.unlock(LOCK);
                        break;
                    }
                }
            }
        });
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i: list){
            System.out.println(i);
        }
    }

    @Test
    public void lock() {
        System.out.println(redisDisLock.lock(LOCK,50000L));
    }

    @Test
    public void unlock() {
        System.out.println(redisDisLock.unlock(LOCK));
    }
}