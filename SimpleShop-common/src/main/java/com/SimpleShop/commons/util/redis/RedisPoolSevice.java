package com.SimpleShop.commons.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisPoolSevice {

    @Autowired(required = false)
    public JedisPool jedisPool;

    public <T> T excute(Function<T,Jedis> fun) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return fun.callBack(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

    public String getKey(String key) {

        return this.excute((Jedis jedis) -> jedis.get(key));
    }

    public String setKey(String key,String value) {

        return this.excute((Jedis jedis)->jedis.set(key,value));
    }

    public Long delKey(String key) {

        return this.excute((Jedis jedis)->jedis.del(key));
    }

    public Long expire(String key,Integer time) {

        return this.excute((Jedis jedis)->jedis.expire(key,time));
    }

    public Long persist(String key,Integer time) {

        return this.excute((Jedis jedis)->jedis.persist(key));
    }

    public Long exists(String... keys ) {

        return this.excute((Jedis jedis)->jedis.exists(keys));
    }

    public Long set(String key,String value,Integer time) {

        return this.excute((Jedis jedis)->{jedis.set(key,value);return jedis.expire(key,time);});

    }

}
