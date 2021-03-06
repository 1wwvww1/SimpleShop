package com.SimpleShop.manage.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object  determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDataSourceKey();
    }
}
