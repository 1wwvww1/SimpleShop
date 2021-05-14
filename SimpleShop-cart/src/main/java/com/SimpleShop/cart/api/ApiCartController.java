package com.SimpleShop.cart.api;

import com.SimpleShop.cart.bean.Cart;
import com.SimpleShop.cart.bean.User;
import com.SimpleShop.cart.bean.UserThreadLocal;
import com.SimpleShop.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author YGH
 */
@RequestMapping("service/api/cart/")
@Controller
public class ApiCartController {

    @Autowired
    public CartService cartService;

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Cart>> SelectCartByUserId(@PathVariable Long userId) {

        try{

            List<Cart> carts = cartService.selectICartByUserId(userId);
            if(carts == null || carts.isEmpty()){

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.status(HttpStatus.OK).body(carts);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
