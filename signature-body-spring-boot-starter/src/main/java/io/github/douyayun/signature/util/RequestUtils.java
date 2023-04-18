package io.github.douyayun.signature.util;

import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;

/**
 * 请求工具
 *
 * @author houp
 * @since 1.0.0
 */
public class RequestUtils {

    private RequestUtils() {

    }

    /**
     * 判断请求的数据类型是否为json
     */
    public static boolean isJson(ServletRequest request) {
        if (request.getContentType() != null) {
            return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
                    request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        return false;
    }


}
