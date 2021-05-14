package com.SimpleShop.web.entiy;

import org.apache.commons.lang3.StringUtils;

public class Item extends com.SimpleShop.manage.entiy.Item {

    public String[] getImages() {
        String images = super.getImage();
        if( null != images ) {
            return StringUtils.split(images,",");
        }

        return null;
    }
}
