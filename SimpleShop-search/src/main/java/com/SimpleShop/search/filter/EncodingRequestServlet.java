package com.SimpleShop.search.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class EncodingRequestServlet extends HttpServletRequestWrapper {

    private HttpServletRequest request;

    public EncodingRequestServlet(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(value == null) {

            return value;
        }
        String method = request.getMethod();
        if("get".equalsIgnoreCase(method)){
            try {
                value = new String(value.getBytes(StandardCharsets.ISO_8859_1),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return value;
    }
}
