package com.SimpleShop.cart.controller;

import com.SimpleShop.cart.bean.Cart;
import com.SimpleShop.cart.bean.User;
import com.SimpleShop.cart.bean.UserThreadLocal;
import com.SimpleShop.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    public CartService cartService;

    @RequestMapping("{itemId}")
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)  {

        User user = UserThreadLocal.get();
        //TODO 商品多数量添加购物车
        int num = 1;
        if(user == null){
            //未登录
            try {

                cartService.addItemToCartWithNoLogin(itemId, num, httpServletRequest, httpServletResponse);
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {

            cartService.addItemToCart(itemId,num);
        }

        return "redirect:/cart/list.html";
    }

    @RequestMapping("list")
    public ModelAndView listCart(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        ModelAndView mv= new ModelAndView("cart");
        User user = UserThreadLocal.get();
        if(user == null){

            try{

                List<Cart> carts = cartService.selectICartByUserIdNoLogin(httpServletRequest, httpServletResponse);
                mv.addObject("cartList", carts);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }else {

            List<Cart> carts = cartService.selectICartByUserId(null);
            mv.addObject("cartList", carts);
        }

        return mv;
    }

    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateCartNum(@PathVariable(value = "itemId") Long itemId,
        @PathVariable(value = "num") Integer num, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        User user = UserThreadLocal.get();
        if(user == null){

            try{

                cartService.updateCartNumNoLogin(itemId, num, httpServletRequest, httpServletResponse);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }else {

            cartService.updateCartNum(itemId, num);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/delete/{itemId}")
    public String deleteItemId(@PathVariable("itemId") Long itemId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        User user = UserThreadLocal.get();
        if(user == null){

            try{

                cartService.deleteItemNoLogin(itemId, httpServletRequest, httpServletResponse);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }else {

            cartService.deleteItem(itemId);
        }

        return "redirect:/cart/show.html";
    }

}
