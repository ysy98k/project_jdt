package com.common;

import javax.servlet.http.HttpServletRequest;

/**
 * 用来保存 rest请求的，request信息
 */
public class RequestHolder {


    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }


    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }

}
