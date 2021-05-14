package com.SimpleShop.manage.datasource;

public class DynamicDataSourceHolder {

    private static final String MASTER = "master";

    //读库对应的数据源key
    private static final String SLAVE = "slave";

    //使用ThreadLocal记录当前线程的数据源key
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void putDataSourceKey(String key) {
        holder.set(key);
    }

    public static String getDataSourceKey() {
        return holder.get();
    }

    public static void markMaster(){
        putDataSourceKey(MASTER);
    }

    public static void markSlave(){
        putDataSourceKey(SLAVE);
    }

}
