package com.github.douyayun.signature;

import com.github.douyayun.signature.filter.RequestBodyReadFilter;
import com.github.douyayun.signature.interceptor.SignatureInterceptor;
import com.github.douyayun.signature.properties.SignatureProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 签名自动配置
 *
 * @author houp
 * @version 1.0.0
 * @date 2023/3/17 20:16
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(SignatureInterceptor.class)
@EnableConfigurationProperties(SignatureProperties.class)
@ConditionalOnProperty(prefix = "signature", value = "enabled", havingValue = "true")
@Slf4j
public class SignatureAutoConfigure implements WebMvcConfigurer {

    @Resource
    private SignatureProperties signatureProperties;

    /**
     * 配置签名拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        validateConfig();
        List<String> excludePaths = signatureProperties.getExcludePaths();
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new SignatureInterceptor(signatureProperties))
                .addPathPatterns(signatureProperties.getIncludePaths());
        if (excludePaths != null && !excludePaths.isEmpty()) {
            interceptorRegistration.excludePathPatterns(excludePaths);
        }
    }

    /**
     * 配置请求体读取过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        validateConfig();
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new RequestBodyReadFilter());
        bean.setName("RequestBodyReadFilter");
        List<String> includePaths = signatureProperties.getIncludePaths();
        includePaths.forEach(includePath -> {
            bean.addUrlPatterns(includePath.replace("**", "*"));
        });
        return bean;
    }

    /**
     * 验证签名配置
     */
    private void validateConfig() {
        List<String> includePaths = signatureProperties.getIncludePaths();
        if (includePaths == null || includePaths.isEmpty()) {
            throw new IllegalArgumentException("signature.includePaths配置不能为空");
        }
//        List<SignatureProperties.SignatureSecretConfig> signatureSecretConfigs = signatureProperties.getSecret();
//        if (signatureSecretConfigs == null || signatureSecretConfigs.isEmpty()) {
//            throw new IllegalArgumentException("signature.secret配置不能为空");
//        }
    }

}
