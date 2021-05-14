package com.SimpleShop.commons.util.redis;

@FunctionalInterface
public interface Function<T,E> {
    public T callBack(E e);
}
