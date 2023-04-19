package io.github.douyayun.signature.config;

import io.github.douyayun.signature.filter.RequestBodyReadFilter;
import io.github.douyayun.signature.manager.SignatureManager;
import io.github.douyayun.signature.properties.SignatureProperties;
import io.github.douyayun.signature.storage.ConfigStorage;
import io.github.douyayun.signature.storage.impl.DefaultConfigStorageImpl;
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
 * @author houp
 * @since 1.0.0
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
        log.info("SignatureManager init...");
        SignatureManager signatureManager = new SignatureManager();
        ConfigStorage defaultConfigStorage = new DefaultConfigStorageImpl(signatureProperties);
        signatureManager.setConfigStorage(defaultConfigStorage);
        return signatureManager;
    }

}
