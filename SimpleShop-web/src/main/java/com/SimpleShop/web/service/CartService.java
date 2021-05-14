package com.SimpleShop.web.service;

import com.SimpleShop.commons.util.httpClient.ApiContentService;
import com.SimpleShop.web.bean.UserThreadLocal;
import com.SimpleShop.web.entiy.Cart;
import com.SimpleShop.web.entiy.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CartService {

    @Autowired
    public ApiContentService apiContentService;

    @Value("CAER.API.URL")
    public String cartUrl;

    public static ObjectMapper mapper = new ObjectMapper();

    public List<Cart> selectCartsByUserId()  {

        User user = UserThreadLocal.get();
        String url = cartUrl+"/service/api/cart/"+user.getId();
        try {

            String carts = apiContentService.doGet(url);
            List<Cart> cartList = mapper.readValue(carts,mapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
            return cartList;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
