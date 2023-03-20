package com.github.douyayun.signature;

import com.github.douyayun.signature.config.SignatureConfig;
import com.github.douyayun.signature.interceptor.SignatureInterceptor;
import com.github.douyayun.signature.manager.SignatureManager;
import com.github.douyayun.signature.properties.SignatureProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;
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
@Import({SignatureConfig.class})
@Slf4j
public class SignatureAutoConfigure implements WebMvcConfigurer {

    @Resource
    private SignatureProperties signatureProperties;

    @Resource
    private SignatureManager signatureManager;

    /**
     * 配置签名拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Assert.notEmpty(signatureProperties.getSecret(), "signature.includePaths配置不能为空");
        List<String> excludePaths = signatureProperties.getExcludePaths();
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new SignatureInterceptor(signatureProperties, signatureManager))
                .addPathPatterns(signatureProperties.getIncludePaths());
        if (excludePaths != null && !excludePaths.isEmpty()) {
            interceptorRegistration.excludePathPatterns(excludePaths);
        }
    }

}
