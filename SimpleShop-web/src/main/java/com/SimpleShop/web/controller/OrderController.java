package com.SimpleShop.web.controller;

import com.SimpleShop.web.entiy.Cart;
import com.SimpleShop.web.entiy.Item;
import com.SimpleShop.web.entiy.Order;
import com.SimpleShop.web.service.CartService;
import com.SimpleShop.web.service.ItemService;
import com.SimpleShop.web.service.OrderService;
import com.SimpleShop.web.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    public ItemService itemService;

    @Autowired
    public UserService userService;

    @Autowired
    public OrderService orderService;

    @Autowired
    public CartService cartService;

    @RequestMapping("{itemId}")
    public ModelAndView toOrder(@PathVariable("itemId") long itemId) {

        ModelAndView modelAndView = new ModelAndView("order");
        try {

            Item item = itemService.queryItemById(itemId);
            if( null == item ) {
                return new ModelAndView("error/exception");
            }
            modelAndView.addObject(item);
        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "submit",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> orderSubmit(Order order) {

        Map data = new HashMap();
        try {
            String orderId = orderService.orderSubmit(order);
            if (StringUtils.isEmpty(orderId)) {

                data.put("status", 400);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
            } else {

                data.put("status", 200);
                data.put("data", orderId);
                return ResponseEntity.status(HttpStatus.OK).body(data);
            }

        } catch ( Exception e ) {

            e.printStackTrace();
        }

        data.put("status", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
    }

    @RequestMapping(value = "success",method = RequestMethod.GET)
    public ModelAndView orderSubmit(@RequestParam("id") String OrderId) {

        if( StringUtils.isEmpty(OrderId) ) {

            return null;
        }
        ModelAndView mv = new ModelAndView("success");
        Order order = orderService.queryByOrderId(OrderId);
        if ( null != order ) {

            mv.addObject("order",order);
            mv.addObject("date",new DateTime().plusDays(3).toString());
        }

        return mv;

    }

    @RequestMapping("create")
    public ModelAndView cartOrder(){

        ModelAndView mv = new ModelAndView("order-cart");
        List<Cart> cartList =cartService.selectCartsByUserId();
        mv.addObject("carts", cartList);

        return mv;
    }
}
