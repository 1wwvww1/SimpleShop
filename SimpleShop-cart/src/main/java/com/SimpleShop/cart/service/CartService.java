package com.SimpleShop.cart.service;

import com.SimpleShop.cart.bean.Cart;
import com.SimpleShop.cart.bean.Item;
import com.SimpleShop.cart.bean.User;
import com.SimpleShop.cart.bean.UserThreadLocal;
import com.SimpleShop.cart.mapper.CartMapper;
import com.SimpleShop.commons.util.JsonAndCookie.CookieUtils;
import com.SimpleShop.commons.util.httpClient.ApiContentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author YGH
 */
@Service
public class CartService {

    @Autowired
    public ApiContentService apiContentService;

    @Autowired
    public CartMapper cartMapper;

    @Autowired
    public ItemService itemService;

    @Value("CART.COOKIE.PREFIX")
    public String cartCookieName;

    public int cookieExpire = 60*60*24*30*12;

    private static ObjectMapper mapper = new ObjectMapper();

    public void addItemToCartWithNoLogin(Long itemId, int num, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        String cartCook = CookieUtils.getCookieValue(httpServletRequest, cartCookieName, true);
        if(StringUtils.isEmpty(cartCook)){

            List<Cart> list = new ArrayList<>();
            Cart cart = new Cart();
            Item item = itemService.queryItemById(itemId);
            cart.setItemId(item.getId());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImages()[0]);
            Date time = new Date();
            cart.setCreated(time);
            cart.setUpdated(time);
            cart.setNum(num);

            String newCartCook =  mapper.writeValueAsString(list);
            CookieUtils.setCookie(httpServletRequest, httpServletResponse, cartCookieName, newCartCook, cookieExpire, true);
        } else {

            List<Cart> listCart = mapper.readValue(cartCook, mapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
            for (Cart cart : listCart) {

                if(cart.getItemId().equals(itemId)) {

                    cart.setNum(cart.getNum()+num);
                }
            }

            CookieUtils.setCookie(httpServletRequest, httpServletResponse, cartCookieName, mapper.writeValueAsString(listCart), cookieExpire,true);
        }
    }

    public void addItemToCart(Long itemId, int num){

        User user = UserThreadLocal.get();
        Long userId = user.getId();
        Cart record = new Cart();
        record.setId(itemId);
        record.setUserId(userId);
        Cart dataCart = cartMapper.selectOne(record);

        if(dataCart == null) {

            Cart cart = new Cart();
            Item item = itemService.queryItemById(itemId);
            cart.setUserId(userId);
            cart.setItemId(item.getId());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImages()[0]);
            Date time = new Date();
            cart.setCreated(time);
            cart.setUpdated(time);
            cart.setNum(num);

            cartMapper.insert(cart);
        } else {

            dataCart.setNum(dataCart.getNum()+num);
            dataCart.setUpdated(new Date());
            cartMapper.updateByPrimaryKey(dataCart);
        }
    }

    public List<Cart> selectICartByUserId(Long userId){

        Example example = new Example(Cart.class);
        example.setOrderByClause("create DESC");
        if(userId==null) {
            User user = UserThreadLocal.get();
            userId = user.getId();
        }
        example.createCriteria().andEqualTo("userId", userId);
        return cartMapper.selectByExample(example);
    }

    public List<Cart> selectICartByUserIdNoLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        String cartCook = CookieUtils.getCookieValue(httpServletRequest, cartCookieName, true);
        if(StringUtils.isEmpty(cartCook)){

            return null;
        }else {

            return mapper.readValue(cartCook, mapper.getTypeFactory().constructCollectionType(List.class,Cart.class));
        }
    }

    public void updateCartNum(Long itemId, Integer num) {

        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("userId", UserThreadLocal.get().getId()
            ).andEqualTo("itemId", itemId);

        Cart cart = new Cart();
        cart.setNum(num);
        cart.setUpdated(new Date());

        cartMapper.updateByExample(cart, example);
    }

    public void updateCartNumNoLogin(Long itemId, Integer num, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        List<Cart> cartList = selectICartByUserIdNoLogin(httpServletRequest, httpServletResponse);
        boolean hasItem = false;
        for (Cart cart : cartList){

            if (cart.getItemId().equals(itemId)){

                cart.setNum(num);
                cart.setUpdated(new Date());
                hasItem = true;
            }
        }

        if (hasItem) {

            CookieUtils.setCookie(httpServletRequest, httpServletResponse, cartCookieName, mapper.writeValueAsString(cartList), cookieExpire,true);
        }
    }

    public void deleteItem(Long itemId) {

        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(UserThreadLocal.get().getId());
        cartMapper.delete(cart);
    }

    public void deleteItemNoLogin(Long itemId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )throws IOException {

        List<Cart> cartList = selectICartByUserIdNoLogin(httpServletRequest, httpServletResponse);
        boolean hasItem = false;
        for (Cart cart : cartList){

            if (cart.getItemId().equals(itemId)){

                cartList.remove(cart);
                hasItem = true;
            }
        }

        if (hasItem) {

            CookieUtils.setCookie(httpServletRequest, httpServletResponse, cartCookieName, mapper.writeValueAsString(cartList), cookieExpire,true);
        }
    }
}
