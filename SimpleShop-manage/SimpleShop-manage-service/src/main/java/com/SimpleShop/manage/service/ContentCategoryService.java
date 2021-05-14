package com.SimpleShop.manage.service;

import com.SimpleShop.manage.entiy.Content;
import com.SimpleShop.manage.entiy.ContentCategory;
import com.SimpleShop.manage.mapper.ContentCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    @Autowired
    public ContentCategoryMapper contentCategoryMapper;

    public List<ContentCategory>  queryContentCategoryByParentId(Long parentId){

        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        List<ContentCategory> result = new ArrayList<ContentCategory>();

        result=contentCategoryMapper.queryContentCategoryByWhere(contentCategory);
        //result = super.queryListByWhere(contentCategory);

        return result;
    }

    /** 保存ContentCategory 需要字段 ParentId */
    public ContentCategory saveContentCategory(ContentCategory contentCategory) {

        ContentCategory parentContentCategory = super.queryById( contentCategory.getParentId() );
        if( null != parentContentCategory && ! parentContentCategory.getIsParent() ) {
            parentContentCategory.setIsParent(true);
            parentContentCategory.setName(null);
            updateContentCategory(parentContentCategory);
        }

        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(contentCategory.getCreated());
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        contentCategory.setId(null);
        Integer res = super.save(contentCategory);

        if(res >0) {
            return contentCategory;
        }
        return null;
    }

    /** 修改ContentCatgory服务接口，未设置字段name与isParent */
    public Integer updateContentCategory(ContentCategory contentCategory) {

        contentCategory.setUpdated(new Date());
        contentCategory.setStatus(null);
        contentCategory.setSortOrder(null);
        contentCategory.setCreated(null);
        contentCategory.setParentId(null);
        //System.out.println(contentCategory);
        Integer res = super.updateSelective(contentCategory);

        return res;
    }

    /**需要ParentId 与 Id*/
    public Integer deleteContentCategory(ContentCategory contentCategory) {

        List<ContentCategory> parentContentC = queryContentCategoryByParentId(contentCategory.getParentId());
        if ( null != parentContentC && parentContentC.size()==1 ) {
            ContentCategory parentContentCategory = new ContentCategory();
            parentContentCategory.setIsParent(false);
            parentContentCategory.setId(contentCategory.getParentId());
            updateContentCategory(parentContentCategory);
        }
        //List<ContentCategory> subContent = queryContentCategoryByParentId(contentCategory.getId());
        List<Object> delBatch = new ArrayList<Object>();
        delBatch.add(contentCategory.getId());
        delBatchByIDSet(delBatch,contentCategory.getId());
        //System.out.println("delBatch"+delBatch);
        Integer res = super.deleteByIds(ContentCategory.class,"id",delBatch);

        return res;
    }

    public void delBatchByIDSet(List<Object> res,Long id) {
        List<ContentCategory> subContent = queryContentCategoryByParentId(id);
        if ( null != subContent) {
            for (  ContentCategory sub : subContent ) {
                res.add(sub.getId());
                if(sub.getIsParent()) {
                    delBatchByIDSet(res, sub.getId());
                }
            }
        }
    }

}
