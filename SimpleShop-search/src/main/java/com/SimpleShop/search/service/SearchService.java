package com.SimpleShop.search.service;

import com.SimpleShop.search.bean.SearchResult;
import com.SimpleShop.search.entiy.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    public HttpSolrServer httpSolrServer;

    public SearchResult selectItem(String keyWord, int page, int rows) {

        SearchResult result = null;
        SolrQuery query = new SolrQuery();
        query.setRows(rows);
        query.setStart(((Math.max(page,1)-1)*rows));
        query.setQuery("title:"+keyWord+" And status:1");

        boolean isHighLight = !StringUtils.isEmpty(keyWord) && !StringUtils.equals("*",keyWord);
        if(isHighLight) {

            query.setHighlight(true);
            query.addHighlightField("title");
            query.setHighlightSimplePre("<em>");
            query.setHighlightSimplePost("</em>");
        }

        try {
            QueryResponse response = httpSolrServer.query(query);
            List<Item> items = response.getBeans(Item.class);

            if (isHighLight) {
                // 将高亮的标题数据写回到数据对象中
                Map<String, Map<String, List<String>>> map = response.getHighlighting();
                for (Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()) {
                    for (Item item : items) {
                        if (!highlighting.getKey().equals(item.getId().toString())) {
                            continue;
                        }
                        item.setTitle(StringUtils.join(highlighting.getValue().get("title"), ""));
                        break;
                    }
                }
            }
            return new SearchResult(response.getResults().getNumFound(), items);
        }catch ( Exception e ) {

            e.printStackTrace();
        }

        return null;
    }
}
