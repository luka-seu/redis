package com.plasticlove.redis.client;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author luka
 * @date 2019/07/11
 * redis 操作客户端
 */
@Component
public class RedisClient {
    private static Logger log = LoggerFactory.getLogger(RedisClient.class);

    private static final int SET_NX_SUCCESS = 1;
    private static final int SET_NX_FAIL = 0;
    private static final String SET_NX_SUCCESS_STR = "OK";

    @Autowired
    private JedisPool jedisPool;
    //key的前缀
    @Value("${redis.key.prefix}")
    private String keyPrefix;


    private Jedis getResource(){
        return jedisPool.getResource();
    }


    /**
     * string 数据类型
     */
    public void set(String key,Object value){
        try {
            //jedis的string类型key和value必须一样类型
            getResource().set(getKey(key), JSON.toJSONString(value));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }
    public Object get(String key) {
        try {
            return JSON.parse(getResource().get(getKey(key)));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * expire
     * @param key
     * @param seconds
     */
    public void expire(String key,int seconds){
        try {
            getResource().expire(getKey(key),seconds);
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
    }
    /**
     * hash数据类型
     */
    public void hset(String key,String filed,Object value){
        try {
            //key+field+value
            getResource().hset(getKey(key),filed,JSON.toJSONString(value));
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
    }

    public Object hget(String key,String field){
        try {
            return JSON.parse(getResource().hget(getKey(key),field));
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
            throw e;
        }
    }
    /**
     * list数据类型
     */
    public void lpush(String key,Object value){
        try {
            getResource().lpush(getKey(key),JSON.toJSONString(value));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public Object lpop(String key){
        try {
            return JSON.parse(getResource().lpop(getKey(key)));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw e;
        }
    }
    /**
     * set数据类型
     */
    /**
     * zset数据类型
     */
    /**
     * delete
     */
    public void del(String key){
        try {
            getResource().del(getKey(key));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    /**
     * hdelete
     * @param key
     * @param field
     */
    public void hdel(String key,String field){
        try {
            getResource().hdel(getKey(key),field);
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
    }
    /**
     * close 务必关闭jedis连接
     */
    public void close(){
        if (getResource().isConnected()){
            getResource().close();
        }
    }
    /**
     * 分布式锁
     */
    public int setNx(String requestId,Object value,long mills){
        try {
            String result = getResource().set(getKey(requestId), JSON.toJSONString(value), "NX", "PX", mills);
            if (StringUtils.equals(result,SET_NX_SUCCESS_STR)){
                return SET_NX_SUCCESS;
            }
            return SET_NX_FAIL;
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
            throw e;
        }

    }




    /**
     * 判断是否连接
     */
    public boolean isConnected(){
        return getResource().isConnected();
    }


    /**
     *
     * @param key
     * @return
     */
    private String getKey(String key){
        return StringUtils.join(keyPrefix,key);
    }
}
