package com.SimpleShop.commons.util.httpClient;

public class HttpClientResult {

    private Integer code;
    private String data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public HttpClientResult(Integer code, String data) {
        this.code = code;
        this.data = data;
    }

    public HttpClientResult() {
    }
}
