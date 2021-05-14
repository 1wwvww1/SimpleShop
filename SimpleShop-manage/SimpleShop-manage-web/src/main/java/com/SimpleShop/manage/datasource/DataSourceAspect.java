package com.SimpleShop.manage.datasource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

public class DataSourceAspect {

    public void before(JoinPoint joinpoint){

        String methodName = joinpoint.getSignature().getName();
        if(isSlaver(methodName)) {

            DynamicDataSourceHolder.markSlave();
        } else {
            // 标记为写库
            DynamicDataSourceHolder.markMaster();
        }
    }

    public boolean isSlaver(String methodName) {

        if(StringUtils.startsWithAny(methodName,"query", "find", "get")) {

            return true;
        }

        return false;
    }
}
