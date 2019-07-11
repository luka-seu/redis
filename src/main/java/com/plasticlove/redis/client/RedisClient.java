package com.plasticlove.redis.client;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.plasticlove.redis.util.ProtoBufUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * @author luka
 * @date 2019/07/11
 * redis 操作客户端
 */
@Component
public class RedisClient {
    private static Logger log = LoggerFactory.getLogger(RedisClient.class);
    @Autowired
    private Jedis jedis;
    //key的前缀
    @Value("${redis.key.prefix}")
    private String keyPrefix;


    /**
     * string 数据类型
     */
    public void set(String key,Object value){
//        try {
            //jedis的string类型key和value必须一样类型
            jedis.set(getKey(key), JSON.toJSONString(value));
//        } catch (Exception e) {
//            log.error(e.getLocalizedMessage());
//        }
    }
    public Object get(String key) {
        try {
            return JSON.parse(getKey(key));
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
            jedis.expire(getKey(key),seconds);
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
            jedis.hset(getKey(key),filed,JSON.toJSONString(value));
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
    }

    public Object hget(String key,String field){
        try {
            return JSON.parse(jedis.hget(getKey(key),field));
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
            jedis.lpush(getKey(key),JSON.toJSONString(value));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public Object lpop(String key){
        try {
            return JSON.parse(jedis.lpop(getKey(key)));
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
            jedis.del(getKey(key));
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
            jedis.hdel(getKey(key),field);
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
    }
    /**
     * close 务必关闭jedis连接
     */
    public void close(){
        if (jedis.isConnected()){
            jedis.close();
        }
    }
    /**
     * 判断是否连接
     */
    public boolean isConnected(){
        return jedis.isConnected();
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
