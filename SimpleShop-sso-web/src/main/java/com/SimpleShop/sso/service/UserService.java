package com.SimpleShop.sso.service;

import com.SimpleShop.commons.util.redis.RedisPoolSevice;
import com.SimpleShop.sso.entiy.User;
import com.SimpleShop.sso.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.util.Date;

@Service
public class UserService extends BaseService<User>{

    @Autowired
    private RedisPoolSevice redisPoolSevice;

    @Autowired
    private UserMapper userMapper;

    private final ObjectMapper MAPPER = new ObjectMapper();

    public Boolean checkRegister(String param,Integer type) {

        User record = new User();
        switch (type) {

            case 1:
                record.setUsername(param);
                break;

            case 2:
                record.setPhone(param);
                break;

            case 3:
                record.setEmail(param);
                break;

            default:
                return null;
        }

        return super.queryOne(record)==null;
    }

    public Boolean register(User user) {

        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        String md5PassW = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(md5PassW);

        return super.saveSelective(user)==1;
    }

    public String login( User user ) throws JsonProcessingException {

        User search = new User();
        search.setUsername(user.getUsername());
        User user1 = this.userMapper.selectOne(search);

        if( null == user1.getUsername() ) {

            return null;
        }

        if( !user1.getPassword().equals( DigestUtils.md5Hex(user.getPassword()) ) ) {

            return null;
        }

        String SIMPLE_TOKEN=System.currentTimeMillis()+user1.getUsername();
        redisPoolSevice.set("SIMPLE_TOKEN_"+SIMPLE_TOKEN,MAPPER.writeValueAsString(user1),60*60);
        return SIMPLE_TOKEN;
    }

    public User queryByToken(String token) throws IOException {

        String key="SIMPLE_TOKEN_"+token;
        String json = redisPoolSevice.getKey(key);
        if( StringUtils.isEmpty(json) ) {

            return null;
        }

        redisPoolSevice.expire(key,60*60);
        return MAPPER.readValue(json,User.class);
    }

}
