/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.web.context.request.RequestContextHolder
 *  org.springframework.web.context.request.ServletRequestAttributes
 */
package com.mdd.common.util;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtils {
    public static HttpServletRequest handler() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    public static String url() {
        HttpServletRequest request = RequestUtils.handler();
        if (request != null) {
            return request.getRequestURL().toString();
        }
        return "";
    }

    public static String uri() {
        String domain = RequestUtils.domain();
        if (!Arrays.asList(443, 80, 0).contains(RequestUtils.port())) {
            domain = domain + ":" + RequestUtils.port();
        }
        return domain;
    }

    public static String route() {
        HttpServletRequest request = RequestUtils.handler();
        if (request != null) {
            return request.getRequestURI();
        }
        return "";
    }

    public static Integer port() {
        HttpServletRequest request = RequestUtils.handler();
        if (request != null) {
            return request.getServerPort();
        }
        return 0;
    }

    public static String domain() {
        HttpServletRequest request = RequestUtils.handler();
        if (request != null) {
            String requestUrl = request.getRequestURL().toString();
            List<String> urls = Arrays.asList(requestUrl.split("/"));
            String agree = "http:";
            String forwardedProto = request.getHeader("X-Forwarded-Proto");
            if (forwardedProto != null && forwardedProto.equalsIgnoreCase("https")) {
                agree = "https:";
            } else if (request.getServerPort() == 443) {
                agree = "https:";
            }
            return agree + "//" + urls.get(2).split(":")[0];
        }
        return null;
    }

    public static String device() {
        HttpServletRequest request = RequestUtils.handler();
        if (request != null) {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("Android")) {
                return "Android";
            }
            if (userAgent.contains("iPhone") || userAgent.contains("iPod") || userAgent.contains("iPad")) {
                return "IOS";
            }
            return "wap";
        }
        return "wap";
    }

    public static Boolean isGet() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("GET");
        }
        return false;
    }

    public static Boolean isPost() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("POST");
        }
        return false;
    }

    public static Boolean isPUT() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("PUT");
        }
        return false;
    }

    public static Boolean isDelete() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getMethod().equals("DELETE");
        }
        return false;
    }
}

