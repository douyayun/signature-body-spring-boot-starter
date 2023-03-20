package com.github.douyayun.signature.config;

import com.github.douyayun.signature.filter.RequestBodyReadFilter;
import com.github.douyayun.signature.manager.SignatureManager;
import com.github.douyayun.signature.properties.SignatureProperties;
import com.github.douyayun.signature.storage.NonceConfigStorage;
import com.github.douyayun.signature.storage.impl.DefaultNonceConfigStorageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author: houp
 * @date: 2023/3/20 21:00
 * @version: 1.0.0
 */
@Configuration
@Slf4j
public class SignatureConfig {

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

    @Bean
    @ConditionalOnMissingBean(SignatureManager.class)
    public SignatureManager signatureManager() {
        log.error("SignatureManager init...");
        SignatureManager signatureManager = new SignatureManager();
        NonceConfigStorage defaultNonceConfigStorage = new DefaultNonceConfigStorageImpl(signatureProperties);
        signatureManager.setNonceConfigStorage(defaultNonceConfigStorage);
        return signatureManager;
    }

}
