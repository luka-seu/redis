package com.plasticlove.redis.dislock;

import com.alibaba.fastjson.JSON;
import com.plasticlove.redis.client.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * redis 分布式锁
 */
@Component
public class RedisDisLock {
    @Autowired
    private RedisClient redisClient;

    private static final String LOCK_VALUE = "DIS_LOCK";
    /**
     * 上锁
     */
    public boolean lock(String requestId,long mills){
        int lockRes = redisClient.setNx(requestId, requestId+LOCK_VALUE, mills);
        if (lockRes==1){
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     */
    public boolean unlock(String requestId){
        String resValue = (String) redisClient.get(requestId);
        //说明lock已过期
        if (StringUtils.isBlank(resValue)){
            return true;
        }else if(StringUtils.equals(resValue,requestId+LOCK_VALUE)){
            //删除key，释放锁
            redisClient.del(requestId);
            return true;
        }else{
            //当前lock的不是此对象
            return false;
        }
    }
}
