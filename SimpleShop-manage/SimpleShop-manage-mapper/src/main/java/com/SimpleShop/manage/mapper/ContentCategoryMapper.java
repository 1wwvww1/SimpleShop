package com.SimpleShop.manage.mapper;

import com.SimpleShop.manage.entiy.ContentCategory;
import com.github.abel533.mapper.Mapper;

import java.util.List;

public interface ContentCategoryMapper extends Mapper<ContentCategory> {

    List<ContentCategory> queryContentCategoryByWhere(ContentCategory contentCategory);

}
