package io.github.douyayun.signature.config;

import io.github.douyayun.signature.filter.RequestBodyReadFilter;
import io.github.douyayun.signature.properties.SignatureProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
@Configuration
public class SignatureWebAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SignatureWebAutoConfiguration.class);

    @Resource
    private SignatureProperties signatureProperties;

    /**
     * 配置请求体读取过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        Assert.notEmpty(signatureProperties.getSecret(), "signature.includePaths配置不能为空");
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new RequestBodyReadFilter());
        bean.setName("RequestBodyReadFilter");
        List<String> includePaths = signatureProperties.getIncludePaths();
        includePaths.forEach(includePath -> {
            bean.addUrlPatterns(includePath.replace("**", "*"));
        });
        return bean;
    }

}
