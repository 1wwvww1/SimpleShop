package com.SimpleShop.cart.bean;

public class UserThreadLocal {

    private static final ThreadLocal threadLocal = new ThreadLocal();

    public static User get() {

        return (User)threadLocal.get();
    }

    public static void set(User user) {

        threadLocal.set(user);
    }

    public static void remove() {

        threadLocal.remove();
    }

}
