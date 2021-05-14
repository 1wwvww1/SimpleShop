package com.SimpleShop.manage.mapper;

import com.SimpleShop.manage.entiy.Content;
import com.github.abel533.mapper.Mapper;

import java.util.List;

public interface ContentMapper extends Mapper<Content> {
    List<Content> queryContentByWhere(Content content);
    void saveContent(Content content);

}
