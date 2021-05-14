package com.SimpleShop.commons.util.httpClient;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO
@Service
public class ApiContentService implements BeanFactoryAware {

    public BeanFactory beanFactory;

    /*@Autowired
    public CloseableHttpClient httpClient;*/

    @Autowired(required = false)
    public RequestConfig requestConfig;

    public String doGet(String url) throws ClientProtocolException,IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        response = getHttpClient().execute(httpGet);

        if( response.getStatusLine().getStatusCode() == 200 ) {
            String res = EntityUtils.toString(response.getEntity(),"UTF-8");
            return res;
        }
        if( null != response ){

            response.close();
        }

        return null;

    }

    public String doGet(String url, Map<String,String> params) throws ClientProtocolException, URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(url);
        if( null != params) {
            for (Map.Entry<String, String> key : params.entrySet()) {
                uriBuilder.setParameter(key.getKey(), key.getValue());
            }
        }

        return doGet(uriBuilder.toString());
    }

    public HttpClientResult doPost(String url,Map<String,String> params) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if( null != params ) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> key : params.entrySet()) {
                param.add(new BasicNameValuePair(key.getKey(), key.getValue()));
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param,"UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
        }

        CloseableHttpResponse response;
        response= getHttpClient().execute(httpPost);
        HttpClientResult result = new HttpClientResult(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(),"UTF-8"));

        if( null != response ){

            response.close();
        }
        return result;

    }

    public CloseableHttpClient getHttpClient() {
        return beanFactory.getBean(CloseableHttpClient.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }
}
