package com.SimpleShop.search.mq;

import com.SimpleShop.search.entiy.Item;
import com.SimpleShop.search.service.ItemService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

public class MQHandler {

    @Autowired
    public HttpSolrServer httpSolrServer;

    @Autowired
    public ItemService itemService;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public void itemHandler(String message){

        try {

            JsonNode node = MAPPER.readTree(message);
            Long id=node.get("id").asLong();
            String type=node.get("type").asText();
            Item item = itemService.selectItemById(id);
            if( "insert".equals(type) || "update".equals(type) ){

                httpSolrServer.addBean(item);
                httpSolrServer.commit();
            }
            if( "delete".equals(type) ) {

                httpSolrServer.deleteById(id.toString());
                httpSolrServer.commit();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
