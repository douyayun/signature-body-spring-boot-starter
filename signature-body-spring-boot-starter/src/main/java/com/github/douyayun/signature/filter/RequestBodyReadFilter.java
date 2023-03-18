package com.github.douyayun.signature.filter;

import com.github.douyayun.signature.util.RequestUtils;
import com.github.douyayun.signature.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求体读取
 *
 * @author houp
 * @version 1.0.0
 * @date 2023/3/17 20:16
 */
@Slf4j
public class RequestBodyReadFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (RequestUtils.isJson(request)) {
            ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

}
