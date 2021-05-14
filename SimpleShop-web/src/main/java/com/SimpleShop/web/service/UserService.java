package com.SimpleShop.web.service;

import com.SimpleShop.commons.util.httpClient.ApiContentService;
import com.SimpleShop.web.entiy.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Value(value = "${SSO.API.URL}")
    public String sso_Url;

    @Autowired(required = false)
    public ApiContentService apiContentService;

    private final ObjectMapper mapper = new ObjectMapper();

    public User queryUserByToken(String Token)  {

        if( StringUtils.isEmpty(Token) ) {

            return null;
        }
        Map param = new HashMap();
        param.put("token",Token);

        try {

            String userStr = apiContentService.doGet(sso_Url+"/api/user",param);
            if ( StringUtils.isEmpty(userStr) ) {

                return  null;
            }
            User user = mapper.readValue(userStr,User.class);
            return user;
        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return null;
    }

}
