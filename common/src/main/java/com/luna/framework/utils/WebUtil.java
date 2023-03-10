package com.luna.framework.utils;

import com.luna.framework.utils.convert.JsonUtil;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtil {

    /**
     * 是否Ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        // 判断是不是APP,是的话返回状态码，不返回登录页
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取服务器地址
     *
     * @param request
     * @return
     */
    public static String getServletPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 发送json格式回复
     *
     * @param response
     * @param obj
     */
    public static void sendJson(HttpServletResponse response, Object obj) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        try {
            JsonUtil.writeJson(response.getWriter(), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送json格式回复，并解决跨域问题
     *
     * @param response
     * @param obj
     * @param allowOrigin
     */
    public static void sendJsonByCors(HttpServletResponse response, HttpStatus httpStatus, Object obj, String allowOrigin) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setStatus(httpStatus.value());
        try {
            if (obj != null) {
                if (obj instanceof String) {
                    response.getWriter().write((String) obj);
                } else {
                    JsonUtil.writeJson(response.getWriter(), obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送json格式回复，并解决跨域问题
     *
     * @param response
     * @param obj
     */
    public static void sendJsonByCors(HttpServletResponse response, HttpStatus httpStatus, Object obj) {
        sendJsonByCors(response, httpStatus, obj, "*");
    }

    /**
     * 判断请求内容是否json格式
     *
     * @param request
     * @return
     */
    public static boolean isJson(HttpServletRequest request) {
        return "application/json".equalsIgnoreCase(request.getContentType());
    }


    /**
     * 从请求中解析json内容
     *
     * @param request
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T parseJsonFromRequest(HttpServletRequest request, Class<T> valueType) {
        try {
            return JsonUtil.parseJson(request.getInputStream(), valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param request
     * @return String
     * @description 获取IP地址
     * <p><strong>注意：</strong>IP地址经过多次反向代理后会有多个IP值，</p>
     * <p>其中第一个IP才是真实IP，所以不能通过request.getRemoteAddr()获取IP地址，</p>
     * <p>如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址</p>
     * <p>X-Forwarded-For中第一个非unknown的有效IP才是用户真实IP地址。</p>
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取多次代理后的IP字符串值
        String XFor = request.getHeader("X-Forwarded-For");
        if (!isEmptyIp(XFor)) {
            // 多次反向代理后会有多个IP值，第一个用户真实的IP地址
            int index = XFor.indexOf(",");
            if (index >= 0) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }

        String ip = request.getHeader("X-Real-IP");
        if (isEmptyIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isEmptyIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isEmptyIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isEmptyIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isEmptyIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isEmptyIp(String ip) {
        return ip == null || ip.length() == 0 || (ip.length() == 7 && "unknown".equalsIgnoreCase(ip));
    }
}
