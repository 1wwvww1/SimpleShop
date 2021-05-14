package com.SimpleShop.web.service;

import com.SimpleShop.commons.util.JsonAndCookie.JsonUtils;
import com.SimpleShop.commons.util.httpClient.ApiContentService;
import com.SimpleShop.commons.util.httpClient.HttpClientResult;
import com.SimpleShop.web.bean.UserThreadLocal;
import com.SimpleShop.web.entiy.Order;

import com.SimpleShop.web.entiy.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    public ApiContentService ApiContentService;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public static final String OrderUrl="http://order.simpleshop.com";

    public String orderSubmit(Order order)  {

        User user = UserThreadLocal.get();
        order.setUserId( user.getId() );
        order.setBuyerNick( user.getUsername() );

        String json = JsonUtils.objectToJson(order);
        Map param = new HashMap();
        param.put("json",json);
        try {

             HttpClientResult result = ApiContentService.doPost(OrderUrl+"/api/order/create",param);
             if ( result.getCode().intValue() != 200 ) {

                 return null;
             }

            String httpResultData = result.getData();
            JsonNode jsonNode = mapper.readTree(httpResultData);
            if( jsonNode.get("status").intValue() != 200  ) {

                return null;
            }
            String orderId = jsonNode.get("data").asText();
            return orderId;
        } catch ( Exception e ) {

            if( logger.isDebugEnabled() ) {
                logger.debug("orderSubmit throws error");
            }
            e.printStackTrace();
        }

        return null;
    }

    public Order queryByOrderId(String orderId) {

        try {

            String result = ApiContentService.doGet(OrderUrl+"/api/order/query/"+orderId);
            if ( !StringUtils.isEmpty(result)) {

                Order order = mapper.readValue(result,Order.class);
                return order;
            }
        } catch ( Exception e ) {

            if( logger.isDebugEnabled() ) {
                logger.debug("queryByOrderId throws error");
            }
            e.printStackTrace();
        }

        return null;
    }
}
