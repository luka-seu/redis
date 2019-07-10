package com.plasticlove.redis.client;

import com.plasticlove.redis.util.ProtoBufUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author luka
 * @date 2019/07/11
 * redis 操作客户端
 * 二进制序列化采用谷歌的protocol buffer
 * json序列化采用fastJson
 */
public class RedisClient {
    private static Logger log = LoggerFactory.getLogger(RedisClient.class);

    private Jedis jedis;
    //key的前缀
    private String keyPrefix;



    /**
     * string 数据类型
     */
    //set
    public void set(String key,Object value){
        try {
            //jedis的string类型key和value必须一样类型
            jedis.set(key.getBytes(), ProtoBufUtils.serialize(value,Object.class));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }finally {
            //释放资源
            jedis.close();
        }
    }
    //get
    public Object get(String key) throws Exception {
        try {
            return ProtoBufUtils.deSerialize(jedis.get(key.getBytes()),Object.class);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new Exception();
        }finally {
            //释放资源
            jedis.close();
        }
    }
    //expire
    /**
     * hash数据类型
     */
    //hset
    //hget
    /**
     * list数据类型
     */
    /**
     * set数据类型
     */
    /**
     * zset数据类型
     */
    //delete
    //
}
