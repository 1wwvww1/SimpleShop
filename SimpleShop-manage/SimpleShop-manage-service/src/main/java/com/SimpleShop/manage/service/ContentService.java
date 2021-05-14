package com.SimpleShop.manage.service;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.commons.util.redis.RedisPoolSevice;
import com.SimpleShop.manage.entiy.Content;
import com.SimpleShop.manage.mapper.ContentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    public ContentMapper contentMapper;

    @Autowired(required = false)
    public RedisPoolSevice redisPoolSevice;

    private final ObjectMapper MAPPER = new ObjectMapper();

    private String redisKeyPre="SIMPLESHOP_MANAGE_CONTENT_";

    @Value("${MANAGE.CONTENT.PERSIST.TIME}")
    private Integer persistTime;

    public EasyUIResult queryContentByCategoryId(Content content,Integer rows,Integer page) {

        try {
            String key=redisKeyPre+content.getCategoryId();
            String data = redisPoolSevice.getKey(key);
            if( !StringUtils.isEmpty(data) ) {
                return EasyUIResult.formatToList(data,Content.class);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        PageHelper.startPage(page,rows);
        Example example = new Example(Content.class);
        example.createCriteria().andEqualTo("categoryId",content.getCategoryId());
        example.setOrderByClause("updated DESC");
        List<Content> contents = contentMapper.selectByExample(example);
        //List<Content> contents = super.queryListByWhere(content);
        PageInfo<Content> pageInfo = new PageInfo<Content>(contents);

        EasyUIResult easyUIResult = new EasyUIResult();
        easyUIResult.setTotal((int) pageInfo.getTotal());
        easyUIResult.setRows(pageInfo.getList());

        try {
            String data = MAPPER.writeValueAsString(easyUIResult);
            redisPoolSevice.set(redisKeyPre+content.getCategoryId(),data,persistTime);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return easyUIResult;

    }

    public void saveContent(Content content) {

        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        content.setId(null);
        super.saveSelective(content);

    }

}
